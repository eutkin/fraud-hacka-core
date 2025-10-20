package com.github.eutkin.sakuga.interpret.exception;

public class NullValueException extends InterpreterException {

    public NullValueException(String expressionReturnNullValue) {
        super(expressionReturnNullValue);
    }
}
