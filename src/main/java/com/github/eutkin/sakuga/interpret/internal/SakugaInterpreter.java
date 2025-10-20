package com.github.eutkin.sakuga.interpret.internal;

import com.github.eutkin.sakuga.SakugaLexer;
import com.github.eutkin.sakuga.SakugaParser;
import com.github.eutkin.sakuga.domain.Attribute;
import com.github.eutkin.sakuga.domain.tree.ConditionElement;
import com.github.eutkin.sakuga.domain.tree.ConditionLeaf;
import com.github.eutkin.sakuga.domain.tree.ConditionNode;
import com.github.eutkin.sakuga.interpret.Interpreter;
import com.github.eutkin.sakuga.interpret.exception.ErrorInterpretationException;
import com.github.eutkin.sakuga.interpret.exception.NullValueException;
import com.github.eutkin.sakuga.interpret.internal.error.RussianANTLRErrorListener;
import com.github.eutkin.sakuga.interpret.internal.value.Value;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenFactory;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;

import java.util.Collection;
import java.util.Map;

@Slf4j
public class SakugaInterpreter implements Interpreter {

    private final CommonTokenFactory tokenFactory;

    public SakugaInterpreter(Collection<Attribute> attributes) {
        this.tokenFactory = new AttributeBasedTokenFactory(attributes);
    }

    @Override
    public Boolean interpret(Map<String, Object> event, ConditionElement element) {
        final var value = this.interpretElement(event, element);
        if (value.isError()) {
            throw new ErrorInterpretationException(value.toString());
        }
        if (value.isNull()) {
            throw new NullValueException("Expression return null value");
        }
        return value.isTrue();
    }

    private Value interpretElement(Map<String, Object> event, ConditionElement element) {
        if (element instanceof ConditionNode node) {
            return node.getChildren().stream()
                    .map(child -> {
                        final var r = interpretElement(event, child);
                        if (child instanceof ConditionLeaf) {
                            log.debug("{}  -   {}", child, r);
                        }
                        return r;
                    })
                    .reduce(Value.TRUE_VALUE, "and".equalsIgnoreCase(node.getLogic()) ? Value::and : Value::or);
        } else {
            return interpretExpression(event, element.toString());
        }
    }

    private Value interpretExpression(Map<String, Object> event, String expression) {
        final var charStream = CharStreams.fromString(expression);
        final var lexer = new SakugaLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new RussianANTLRErrorListener());
        lexer.setTokenFactory(this.tokenFactory);
        final var tokenStream = new CommonTokenStream(lexer);
        SakugaParser parser = new SakugaParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(new RussianANTLRErrorListener());
        parser.setErrorHandler(new DefaultErrorStrategy());
        final var line = parser.line();
        final var visitor = new SakugaVisitor(event);
        return visitor.run(line);
    }
}
