package com.github.eutkin.sakuga.interpret.exception;

public abstract class InterpreterException extends RuntimeException {

    public InterpreterException() {
    }

    public InterpreterException(String message) {
        super(message);
    }

    public InterpreterException(String message, Throwable cause) {
        super(message, cause);
    }
}
