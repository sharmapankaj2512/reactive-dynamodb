package com.reactive.dynamodb;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public abstract class DynamoKey {
    private final String name;
    private final String value;

    public DynamoKey(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String name() {
        return name;
    }

    public String value() {
        return value;
    }

    public Map<String, Condition> toConditionMap() {
        Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS(value()));
        return ImmutableMap.of(name(), condition);
    }

    public ImmutableMap<String, AttributeValue> toDynamoKeyMap() {
        return ImmutableMap.of(name(), new AttributeValue(value()));
    }
}

