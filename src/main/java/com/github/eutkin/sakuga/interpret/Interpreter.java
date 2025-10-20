package com.github.eutkin.sakuga.interpret;

import com.github.eutkin.sakuga.domain.tree.ConditionElement;
import com.github.eutkin.sakuga.interpret.exception.InterpreterException;

import java.util.Map;

public interface Interpreter {

    Boolean interpret(Map<String, Object> event, ConditionElement element) throws InterpreterException;
}
