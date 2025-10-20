package com.github.eutkin.sakuga.domain.tree;

public class ConditionLeaf implements ConditionElement {

    private final String type = "leaf";
    private final String leftOperand;
    private final String operator;
    private final String rightOperand;

    public ConditionLeaf(String leftOperand, String operator, String rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }

    @Override
    public String toString() {
        return leftOperand + " " + operator + " " + rightOperand;
    }
}
