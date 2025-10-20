package com.github.eutkin.sakuga.endpont.rule;

import com.github.eutkin.sakuga.domain.Rule;

import java.util.Map;
import java.util.Optional;

public interface RuleEngine {

    Optional<Rule> run(Map<String, Object> event);
}
