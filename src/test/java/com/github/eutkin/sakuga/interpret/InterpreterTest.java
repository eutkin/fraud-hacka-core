package com.github.eutkin.sakuga.interpret;

import com.github.eutkin.sakuga.domain.Attribute;
import com.github.eutkin.sakuga.domain.tree.ConditionElement;
import com.github.eutkin.sakuga.domain.tree.ConditionLeaf;
import com.github.eutkin.sakuga.interpret.internal.SakugaInterpreter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class InterpreterTest {

    private final Interpreter interpreter = new SakugaInterpreter(List.of(
            new Attribute("string", "string"),
            new Attribute("int", "int"),
            new Attribute("float", "float"),
            new Attribute("null-string", "string")
    ));

    private final Map<String, Object> event = new HashMap<>(Map.of(
            "string", "test",
            "int", 10,
            "float", 10.00
    ));

    @ParameterizedTest
    @MethodSource("source")
    void testPositive(ConditionElement element) {
        Boolean result = this.interpreter.interpret(event, element);
        assertTrue(result);
    }

    public static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of(new ConditionLeaf(":string:", "=", "'test'" )),
                Arguments.of(new ConditionLeaf(":string:", ">", "'abc'")),
                Arguments.of(new ConditionLeaf(":string:", "<", "'xyz'")),
                Arguments.of(new ConditionLeaf(":string:", "<=", "'test'" )),
                Arguments.of(new ConditionLeaf(":string:", ">=", "'test'" )),
                Arguments.of(new ConditionLeaf(":string:", ">=", "'abc'")),
                Arguments.of(new ConditionLeaf(":string:", "<=", "'xyz'")),
                Arguments.of(new ConditionLeaf(":int:", "=", "10")),
                Arguments.of(new ConditionLeaf(":int:", "=", "10.0")),
                Arguments.of(new ConditionLeaf(":int:", "<", "15")),
                Arguments.of(new ConditionLeaf(":int:", ">", "5")),
                Arguments.of(new ConditionLeaf(":int:", "<=", "10")),
                Arguments.of(new ConditionLeaf(":int:", ">=", "5")),
                Arguments.of(new ConditionLeaf(":int:", "=", "10.0")),
                Arguments.of(new ConditionLeaf(":int:", "<=", "10")),
                Arguments.of(new ConditionLeaf(":int:", ">=", "10")),
                Arguments.of(new ConditionLeaf(":int:", "<", "10.5")),
                Arguments.of(new ConditionLeaf(":float:", "=", "10")),
                Arguments.of(new ConditionLeaf(":float:", "=", "10.0")),
                Arguments.of(new ConditionLeaf(":float:", "<", "15")),
                Arguments.of(new ConditionLeaf(":float:", ">", "5")),
                Arguments.of(new ConditionLeaf(":float:", "<=", "10")),
                Arguments.of(new ConditionLeaf(":float:", ">=", "5")),
                Arguments.of(new ConditionLeaf(":float:", "=", "10.0")),
                Arguments.of(new ConditionLeaf(":float:", "<=", "10")),
                Arguments.of(new ConditionLeaf(":float:", ">=", "10")),
                Arguments.of(new ConditionLeaf(":float:", "<", "10.5"))

        );
    }
}