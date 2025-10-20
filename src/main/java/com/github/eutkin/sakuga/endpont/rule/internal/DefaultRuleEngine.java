package com.github.eutkin.sakuga.endpont.rule.internal;

import com.github.eutkin.sakuga.domain.Rule;
import com.github.eutkin.sakuga.interpret.Interpreter;
import com.github.eutkin.sakuga.state.RuleState;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Singleton
@Slf4j
public class DefaultRuleEngine {

    private final RuleState ruleState;

    private final Provider<Interpreter> interpreterProvider;

    public DefaultRuleEngine(RuleState ruleState, Provider<Interpreter> interpreterProvider) {
        this.ruleState = ruleState;
        this.interpreterProvider = interpreterProvider;
    }

    public Optional<Rule> run(Map<String, Object> event) {
        return this.ruleState.rules().stream().filter(rule -> {
            try {
                return interpretRule(rule, event);
            } catch (Exception ex) {
                return false;
            }
        }).findFirst();
    }

    private Boolean interpretRule(Rule rule, Map<String, Object> event) {
        return this.interpreterProvider.get().interpret(event, rule.condition());
    }

}
