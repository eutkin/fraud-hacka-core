package com.github.eutkin.sakuga.state.internal;

import com.github.eutkin.sakuga.domain.Rule;
import com.github.eutkin.sakuga.domain.tree.ConditionLeaf;
import com.github.eutkin.sakuga.domain.tree.ConditionNode;
import com.github.eutkin.sakuga.state.RuleState;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.UUID;

import static com.github.eutkin.sakuga.domain.RecommendedAction.DENY;

@Singleton
public class MockRuleState implements RuleState {

    @Override
    public List<Rule> rules() {
        return List.of(
                new Rule(
                        UUID.randomUUID().toString(),
                        1,
                        "Test rule 1",
                        1,
                        DENY,
                        new ConditionNode("AND", List.of(
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
                        ))
                )
        );
    }
}
