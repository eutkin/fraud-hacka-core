package com.github.eutkin.sakuga.endpont.rule;

import com.github.eutkin.sakuga.domain.Rule;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@Controller("/api/v1")
@RequiredArgsConstructor
public class EventEndpoint {

    private final RuleEngine ruleEngine;

    @Post("/rule")
    public HttpResponse<?> check(@Body Map<String, Object> event) {
        Optional<Rule> triggeredRule = this.ruleEngine.run(event);
        if (triggeredRule.isPresent()) {
            return HttpResponse.ok(triggeredRule.map(Rule::id).get());
        }
        return HttpResponse.noContent();
    }
}
