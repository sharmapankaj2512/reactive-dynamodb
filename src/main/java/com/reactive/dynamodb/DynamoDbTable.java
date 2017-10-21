package com.reactive.dynamodb;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class DynamoDbTable {
    private final String name;
    private final DynamoDbHashKey hashKey;
    private final DynamoDbHashKey rangeKey;

    public DynamoDbTable(String name, DynamoDbHashKey hashKey) {
        this(name, hashKey, null);
    }

    public DynamoDbTable(String name, DynamoDbHashKey hashKey, DynamoDbHashKey rangeKey) {
        this.name = name;
        this.hashKey = hashKey;
        this.rangeKey = rangeKey;
    }

    public String getName() {
        return name;
    }

    public ImmutableMap<String, AttributeValue> toDynamoKeyMap() {
        ImmutableMap.Builder<String, AttributeValue> builder = ImmutableMap.builder();
        if (hashKey != null) builder.putAll(hashKey.toDynamoKeyMap());
        if (rangeKey != null) builder.putAll(rangeKey.toDynamoKeyMap());
        return builder.build();
    }

    public Map<String, Condition> toDynamoConditionMap() {
        ImmutableMap.Builder<String, Condition> builder = ImmutableMap.builder();
        if (hashKey != null) builder.putAll(hashKey.toConditionMap());
        if (rangeKey != null) builder.putAll(rangeKey.toConditionMap());
        return builder.build();
    }
}
