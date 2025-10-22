package com.github.eutkin.sakuga.managment;

import com.github.eutkin.sakuga.domain.Rule;
import com.github.eutkin.sakuga.state.RuleState;
import io.micronaut.management.endpoint.annotation.Endpoint;
import io.micronaut.management.endpoint.annotation.Read;

import java.util.List;

@Endpoint(value = "rule", prefix = "custom")
public class RuleStateEndpoint {

    private final RuleState ruleState;

    public RuleStateEndpoint(RuleState ruleState) {
        this.ruleState = ruleState;
    }

    @Read
    public List<Rule> viewRules() {
        return this.ruleState.rules();
    }
}
