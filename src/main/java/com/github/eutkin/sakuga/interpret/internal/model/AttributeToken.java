package com.github.eutkin.sakuga.interpret.internal.model;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.misc.Pair;

public class AttributeToken extends CommonToken {

    private final String attributeName;

    public AttributeToken(String attributeName,
                          Pair<TokenSource, CharStream> source,
                          int type,
                          int channel,
                          int start,
                          int stop
    ) {
        super(source, type, channel, start, stop);
        this.attributeName = attributeName;
    }

    public AttributeToken(String attributeName, int type, String text) {
        super(type, text);
        this.attributeName = attributeName;
    }


    public String getAttributeName() {
        return attributeName;
    }
}
