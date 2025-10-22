package com.github.eutkin.sakuga.income.rule.internal;

import com.github.eutkin.sakuga.domain.Attribute;
import com.github.eutkin.sakuga.domain.Rule;
import com.github.eutkin.sakuga.income.rule.RuleEngine;
import com.github.eutkin.sakuga.interpret.Interpreter;
import com.github.eutkin.sakuga.state.RuleState;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.message.StringMapMessage;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Singleton
@Log4j2
public class DefaultRuleEngine implements RuleEngine {

    private final RuleState ruleState;

    private final Provider<Interpreter> interpreterProvider;

    public DefaultRuleEngine(RuleState ruleState, Provider<Interpreter> interpreterProvider) {
        this.ruleState = ruleState;
        this.interpreterProvider = interpreterProvider;
    }

    @Override
    public Optional<Rule> run(Map<String, Object> event) {
        for (Rule rule : this.ruleState.rules()) {
            try {
                Boolean result = this.interpretRule(rule, event);
                if (result) {
                    log.trace(new StringMapMessage(Map.of(
                            "message", "rule was triggered",
                            "event-id", Objects.toString(event.get(Attribute.ID)),
                            "rule-id", rule.id(),
                            "rule-name", rule.name()
                    )));
                    return Optional.of(rule);
                }
            } catch (Exception ex) {
                log.error(new StringMapMessage(Map.of(
                        "message", ex.getMessage(),
                        "event-id", Objects.toString(event.get(Attribute.ID)),
                        "rule-id", rule.id(),
                        "rule-name", rule.name()
                )), ex);
            }
        }
        return Optional.empty();
    }

    private Boolean interpretRule(Rule rule, Map<String, Object> event) {
        return this.interpreterProvider.get().interpret(event, rule.condition());
    }

}
