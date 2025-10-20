package com.github.eutkin.sakuga.interpret.internal;

import com.github.eutkin.sakuga.domain.tree.ConditionElement;

import java.util.Map;

public interface Interpreter {

    Boolean interpret(Map<String, Object> event, ConditionElement element);
}
