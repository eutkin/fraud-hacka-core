package com.github.eutkin.sakuga.state.internal;

import com.github.eutkin.sakuga.domain.Attribute;
import com.github.eutkin.sakuga.state.AttributeState;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class MockAttributeState implements AttributeState {

    private final AtomicReference<List<Attribute>> attributesRef = new AtomicReference<>(
            List.of(
                    new Attribute("type", "string"),
                    new Attribute("channel", "string"),
                    new Attribute("amount", "float")
            )
    );

    @Override
    public List<Attribute> attributes() {
        return this.attributesRef.get();
    }

    @Override
    public void add(Attribute attribute) {
        this.attributesRef.getAndUpdate(oldList -> {
                    ArrayList<Attribute> newList = new ArrayList<>(oldList);
                    newList.add(attribute);
                    return newList;
                }
        );
    }
}
