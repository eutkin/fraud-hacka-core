package com.github.eutkin.sakuga.interpret;

import com.github.eutkin.sakuga.interpret.internal.SakugaInterpreter;
import com.github.eutkin.sakuga.state.AttributeState;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Prototype;

@Factory
class InterpreterFactory {

    private final AttributeState state;

    public InterpreterFactory(AttributeState state) {
        this.state = state;
    }

    @Prototype
    Interpreter interpreter() {
        return new SakugaInterpreter(this.state.attributes());
    }
}
