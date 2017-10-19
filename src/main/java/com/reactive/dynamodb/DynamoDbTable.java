package com.reactive.dynamodb;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.collect.ImmutableMap;

public class DynamoDbTable {
    private final String name;
    private final DynamoDbHashKey dynamoDbHashKey;

    public DynamoDbTable(String name, DynamoDbHashKey dynamoDbHashKey) {
        this.name = name;
        this.dynamoDbHashKey = dynamoDbHashKey;
    }

    public ImmutableMap<String, AttributeValue> toDynamoKeyMap() {
        return ImmutableMap.of(
                dynamoDbHashKey.name(),
                new AttributeValue(dynamoDbHashKey.value()));
    }

    public String getName() {
        return name;
    }
}
