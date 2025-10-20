package com.github.eutkin.sakuga.interpret.internal;

import com.github.eutkin.sakuga.SakugaLexer;
import com.github.eutkin.sakuga.SakugaParser;
import com.github.eutkin.sakuga.domain.Attribute;
import com.github.eutkin.sakuga.domain.tree.ConditionElement;
import com.github.eutkin.sakuga.domain.tree.ConditionLeaf;
import com.github.eutkin.sakuga.domain.tree.ConditionNode;
import com.github.eutkin.sakuga.interpret.internal.value.Value;
import org.antlr.v4.runtime.*;

import java.util.Collection;
import java.util.Map;

public class SakugaInterpreter implements Interpreter {

    private final CommonTokenFactory tokenFactory;

    public SakugaInterpreter(Collection<Attribute> attributes) {
        this.tokenFactory = new AttributeBasedTokenFactory(attributes);
    }

    @Override
    public Boolean interpret(Map<String, Object> event, ConditionElement element) {
        final var value = this.interpretElement(event, element);
        if (value.isError()) {
            throw new RuntimeException(value.toString());
        }
        if (value.isNull()) {
            throw new NullPointerException("Expression return null value");
        }
        return value.isTrue();
    }

    private Value interpretElement(Map<String, Object> event, ConditionElement element) {
        if (element instanceof ConditionNode node) {
            Value result = node.getChildren().stream()
                    .map(child -> {
                        final var r = interpretElement(event, child);
                        if (child instanceof ConditionLeaf) {
                            System.out.println(child + "  -   " + r);
                        }
                        return r;
                    })
                    .reduce(Value.TRUE_VALUE, "and".equalsIgnoreCase(node.getLogic()) ? Value::and : Value::or);
            return result;
        } else {
            return interpretExpression(event, element.toString());
        }
    }

    private Value interpretExpression(Map<String, Object> event, String expression) {
        final var charStream = CharStreams.fromString(expression);
        final var lexer = new SakugaLexer(charStream);
        lexer.setTokenFactory(this.tokenFactory);
        final var tokenStream = new CommonTokenStream(lexer);
        SakugaParser parser = new SakugaParser(tokenStream);
        final var line = parser.line();
        final var visitor = new SakugaVisitor(event);
        return visitor.run(line);
    }
}
