package com.github.eutkin.sakuga.interpret;

import com.github.eutkin.sakuga.domain.tree.ConditionLeaf;
import com.github.eutkin.sakuga.domain.tree.ConditionNode;
import com.github.eutkin.sakuga.interpret.internal.Interpreter;
import com.github.eutkin.sakuga.interpret.internal.SakugaInterpreter;
import com.github.eutkin.sakuga.state.AttributeState;
import com.github.eutkin.sakuga.state.internal.MockAttributeState;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class SakugaInterpreterTest {

    @Test
    void toStringTest() {
        final var root = new ConditionNode("AND", List.of(
                new ConditionLeaf(":type:", "=", "'PAYMENT'"),
                new ConditionNode("OR", List.of(
                        new ConditionLeaf(":channel:", "=", "'WEB'"),
                        new ConditionLeaf(":channel:", "=", "'MOBILE'")
                )),
                new ConditionNode("AND", List.of(
                        new ConditionLeaf(":amount:", ">", "0"),
                        new ConditionLeaf(":amount:", "<", "1000")
                )),
                new ConditionNode("AND", List.of(
                        new ConditionLeaf(":amount:", ">=", "0"),
                        new ConditionLeaf(":amount:", "<=", "100.420")
                ))
        ));
        System.out.println(root);

        final var event = Map.<String, Object>of(
                "type", "PAYMENT",
                "channel", "WEB",
                "amount", 100.42
        );

        AttributeState attributeState = new MockAttributeState();
        Interpreter interpreter = new SakugaInterpreter(attributeState.attributes());
        Boolean result = interpreter.interpret(event, root);
        System.out.println(result);
    }


}