package com.github.eutkin.sakuga.income;

import com.github.eutkin.sakuga.domain.Attribute;
import com.github.eutkin.sakuga.domain.Rule;
import com.github.eutkin.sakuga.income.rule.RuleEngine;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetStrategy;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import io.micronaut.messaging.Acknowledgement;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@KafkaListener(
        groupId = "${income.event.consumer.group_id}",
        batch = true,
        threadsValue = "${income.event.consumer.thread_count:1}",
        offsetStrategy = OffsetStrategy.DISABLED
)
@RequiredArgsConstructor
@Requires(property = "outcome.event.producer.topic")
@Requires(property = "income.event.consumer.topic")
@Requires(property = "income.event.consumer.group_id")
public class IncomeEventConsumer {


    private final RuleEngine ruleEngine;

    private final Producer<String, Map<String, Object>> eventProducer;

    @Value("${outcome.event.producer.topic}")
    private final String outcomeTopic;

    @Value("${outcome.event.produce.timeout:100ms}")
    private final Duration produceTimeout;

    @Topic("${income.event.consumer.topic}")
    public void receive(
            List<ConsumerRecord<String, Map<String, Object>>> records,
            Acknowledgement ack
    ) {
        final var callbacks = new CompletableFuture<?>[records.size()];
        for (int i = 0; i < records.size(); i++) {
            var record = records.get(i);
            // здесь нужно убедиться, что Map изменяема, но пока это опустим, так как Jackson использует HashMap
            final var event = record.value();

            Optional<Rule> result = this.ruleEngine.run(event);
            result.ifPresent(rule -> {
                event.put(Attribute.MAIN_RULE_ID, rule.id());
            });

            final var outcomeRecord = new ProducerRecord<>(
                    this.outcomeTopic,
                    record.partition(),
                    record.key(),
                    event
            );

            CompletableFuture<Object> waiting = new CompletableFuture<>();
            this.eventProducer.send(outcomeRecord, (metadata, exception) -> {
                if (exception != null) {
                    waiting.completeExceptionally(exception);
                } else {
                    waiting.complete(metadata);
                }
            });
            callbacks[i] = waiting;
        }

        try {
            CompletableFuture.allOf(callbacks).get(
                    this.produceTimeout.toMillis(),
                    TimeUnit.MILLISECONDS
            );
            this.eventProducer.flush();
            ack.ack();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            ack.nack();
        }

    }

}
