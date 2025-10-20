package com.github.eutkin.sakuga.interpret.internal;

import com.github.eutkin.sakuga.SakugaParser;
import com.github.eutkin.sakuga.SakugaParserBaseVisitor;
import com.github.eutkin.sakuga.interpret.internal.model.AttributeToken;
import com.github.eutkin.sakuga.interpret.internal.value.Value;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Map;
import java.util.function.BiFunction;

public class SakugaVisitor extends SakugaParserBaseVisitor<Value> {


    private Value value = Value.FALSE_VALUE;

    private final Map<String, Object> event;

    private final static Map<Integer, BiFunction<Value, Value, Value>> FUNCTION_CACHE = Map.of(
            SakugaParser.EQUALS, Value::eq,
            SakugaParser.NOT_EQUALS, Value::notEq,
            SakugaParser.GREATER, Value::gt,
            SakugaParser.LESS, Value::lt,
            SakugaParser.GREATER_OR_EQUALS, Value::gte,
            SakugaParser.LESS_OR_EQUALS, Value::lte
    );

    public SakugaVisitor(Map<String, Object> event) {
        this.event = event;
    }

    public Value run(ParseTree tree) {
        this.visit(tree);
        return this.value;
    }

    @Override
    public Value visit(ParseTree tree) {
        return super.visit(tree);
    }


    @Override
    public Value visitExpression(SakugaParser.ExpressionContext ctx) {
       final var value = super.visitExpression(ctx);
        this.value = value != null ? value : Value.FALSE_VALUE;
        return value;
    }

    @Override
    public Value visitStringExpression(SakugaParser.StringExpressionContext ctx) {
        Value left = visit(ctx.getChild(0));
        Value right = visit(ctx.getChild(2));
        return FUNCTION_CACHE.getOrDefault(ctx.op.getType(), (a, b) -> Value.FALSE_VALUE)
                .apply(left, right);
    }

    @Override
    public Value visitNumberExpression(SakugaParser.NumberExpressionContext ctx) {
        Value left = visit(ctx.getChild(0));
        Value right = visit(ctx.getChild(2));
        return FUNCTION_CACHE.getOrDefault(ctx.op.getType(), (a, b) -> Value.FALSE_VALUE)
                .apply(left, right);
    }

    @Override
    public Value visitStringLiteral(SakugaParser.StringLiteralContext ctx) {
        String text = ctx.getChild(0).getText();
        return Value.fromString(text.substring(1, text.length() - 1));
    }

    @Override
    public Value visitStringAttribute(SakugaParser.StringAttributeContext ctx) {
        return this.valueFromContext(ctx.getChild(0));
    }

    @Override
    public Value visitNumberLiteral(SakugaParser.NumberLiteralContext ctx) {
        String text = ctx.getChild(0).getText();
        return Value.fromNumber(Float.parseFloat(text));
    }

    @Override
    public Value visitNumberAttribute(SakugaParser.NumberAttributeContext ctx) {
        return this.valueFromContext(ctx.getChild(0));
    }

    private Value valueFromContext(ParseTree tree) {
        Object payload = tree.getPayload();
        if (payload instanceof AttributeToken token) {
            String attributeName = token.getAttributeName();
            Object value = this.event.get(attributeName);
            return Value.fromObject(value);
        }
        throw new RuntimeException("Unknown");
    }
}
