package com.github.eutkin.sakuga.endpont;

import com.github.eutkin.sakuga.domain.Rule;
import com.github.eutkin.sakuga.endpont.rule.internal.DefaultRuleEngine;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.message.ObjectMessage;

import java.util.Map;
import java.util.Optional;

@Controller("/api/v1")
@RequiredArgsConstructor
@Log4j2
public class EventEndpoint {

    private final DefaultRuleEngine ruleEngine;

    @Post("/rule")
    public HttpResponse<?> check(@Body Map<String, Object> event) {
        Optional<Rule> triggeredRule = this.ruleEngine.run(event);
        if (triggeredRule.isPresent()) {
            return HttpResponse.ok(triggeredRule.map(Rule::id).get());
        }
        return HttpResponse.noContent();
    }
}
