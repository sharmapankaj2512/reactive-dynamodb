package com.reactive.dynamodb;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

abstract class DynamoKey {
    private final String name;
    private final String value;

    DynamoKey(String name, String value) {
        this.name = name;
        this.value = value;
    }

    private String name() {
        return name;
    }

    private String value() {
        return value;
    }

    Map<String, Condition> toConditionMap() {
        Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS(value()));
        return ImmutableMap.of(name(), condition);
    }

    ImmutableMap<String, AttributeValue> toDynamoKeyMap() {
        return ImmutableMap.of(name(), new AttributeValue(value()));
    }
}

