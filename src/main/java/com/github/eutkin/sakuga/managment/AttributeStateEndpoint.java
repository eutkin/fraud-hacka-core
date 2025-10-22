package com.github.eutkin.sakuga.managment;

import com.github.eutkin.sakuga.domain.Attribute;
import com.github.eutkin.sakuga.state.AttributeState;
import io.micronaut.management.endpoint.annotation.Endpoint;
import io.micronaut.management.endpoint.annotation.Read;

import java.util.List;

@Endpoint(value = "attribute", prefix = "custom")
public class AttributeStateEndpoint {

    private final AttributeState attributeState;

    public AttributeStateEndpoint(AttributeState attributeState) {
        this.attributeState = attributeState;
    }

    @Read
    public List<Attribute> viewAttributes() {
        return this.attributeState.attributes();
    }
}
