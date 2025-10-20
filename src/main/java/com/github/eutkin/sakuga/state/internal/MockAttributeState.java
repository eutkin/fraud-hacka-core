package com.github.eutkin.sakuga.state.internal;

import com.github.eutkin.sakuga.domain.Attribute;
import com.github.eutkin.sakuga.state.AttributeState;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class MockAttributeState implements AttributeState {


    @Override
    public List<Attribute> attributes() {
        return List.of(
                new Attribute("type", "string"),
                new Attribute("channel", "string"),
                new Attribute("amount", "float")
        );
    }
}
