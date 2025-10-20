package com.github.eutkin.sakuga.interpret.open;

import com.github.eutkin.sakuga.interpret.internal.Interpreter;
import com.github.eutkin.sakuga.interpret.internal.SakugaInterpreter;
import com.github.eutkin.sakuga.state.AttributeState;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Prototype;

@Factory
public class InterpreterAPI {

    private final AttributeState state;

    public InterpreterAPI(AttributeState state) {
        this.state = state;
    }

    @Prototype
    Interpreter interpreter() {
        return new SakugaInterpreter(this.state.attributes());
    }
}
