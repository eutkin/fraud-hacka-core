package com.github.eutkin.sakuga.endpont.rule;

import com.github.eutkin.sakuga.domain.Rule;
import com.github.eutkin.sakuga.interpret.internal.Interpreter;
import com.github.eutkin.sakuga.state.RuleState;
import jakarta.inject.Singleton;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
@Log4j2
public class RuleEngine {

    private final RuleState ruleState;

    private final Interpreter interpreter;

    public RuleEngine(RuleState ruleState, Interpreter interpreter) {
        this.ruleState = ruleState;
        this.interpreter = interpreter;
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
        return this.interpreter.interpret(event, rule.condition());
    }

}
