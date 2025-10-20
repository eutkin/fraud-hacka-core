package com.github.eutkin.sakuga.interpret.internal;

import com.github.eutkin.sakuga.domain.Attribute;
import com.github.eutkin.sakuga.interpret.internal.model.AttributeToken;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenFactory;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.Pair;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class AttributeBasedTokenFactory extends CommonTokenFactory {


    private final Map<String, Attribute> attributes;

    private final Map<String, Integer> typeMapping = Map.of(
            "boolean",2,
            "int", 3,
            "float", 3,
            "string",4
    );

    public AttributeBasedTokenFactory(Collection<Attribute> attributes) {
        this.attributes = attributes.stream().collect(Collectors.toMap(Attribute::name, a -> a));
    }

    @Override
    public CommonToken create(Pair<TokenSource, CharStream> source, int type, String text, int channel, int start, int stop, int line, int charPositionInLine) {
        if (type == 1) {
            String txt = source.b.getText(Interval.of(start, stop));
            String name = txt.substring(1, txt.length() - 1);
            Integer newType = this.typeMapping.get(this.attributes.get(name).type());
            if (newType == null) {
                throw new RuntimeException("unknown attribute name: " + text);
            }
            AttributeToken token = new AttributeToken(name, source, newType, channel, start, stop);
            token.setCharPositionInLine(charPositionInLine);
            token.setText(txt);
            token.setLine(line);
            return token;
        }
        return super.create(source, type, text, channel, start, stop, line, charPositionInLine);
    }
}
