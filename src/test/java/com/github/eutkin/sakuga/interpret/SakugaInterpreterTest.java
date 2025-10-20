package com.github.eutkin.sakuga.interpret;

import com.github.eutkin.sakuga.domain.tree.ConditionLeaf;
import com.github.eutkin.sakuga.domain.tree.ConditionNode;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

@MicronautTest
class SakugaInterpreterTest {

    @Inject
    private Provider<Interpreter> interpreter;

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

        Boolean result = interpreter.get().interpret(event, root);

        System.out.println(result);
    }


}