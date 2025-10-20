package com.github.eutkin.sakuga.domain.tree;

import java.util.ArrayList;
import java.util.List;

public class ConditionNode implements ConditionElement {

    private final String type = "node";
    private final String logic;
    private final List<ConditionElement> children;

    public ConditionNode(String logic, List<ConditionElement> children) {
        this.logic = logic;
        this.children = children;
    }

    public String getLogic() {
        return logic;
    }

    public List<ConditionElement> getChildren() {
        return children;
    }

    public String toString() {
        return this.toString(0);
    }

    public String toString(int level) {
        String prefix = "    ".repeat(level);
        List<String> lines = new ArrayList<>();

        for (int i = 0; i < children.size(); i++) {
            if (i > 0) {
                lines.add(prefix + this.logic);
            }
            if (children.get(i) instanceof ConditionNode node) {
                lines.add(node.toString(level + 1));
            } else {
                lines.add(prefix + children.get(i).toString());
            }
        }
        return String.join("\n", lines);
    }
}
