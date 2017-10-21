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

    public ImmutableMap<String, AttributeValue> toDynamoKeyMap() {
        if (rangeKey == null) return ImmutableMap.of(hashKey.name(), new AttributeValue(hashKey.value()));
        return ImmutableMap.of(hashKey.name(), new AttributeValue(hashKey.value()),
                rangeKey.name(), new AttributeValue(rangeKey.value()));
    }

    public String getName() {
        return name;
    }

    public Map<String, Condition> conditions() {
        ImmutableMap.Builder<String, Condition> builder = ImmutableMap.<String, Condition>builder();
        if (hashKey != null) builder.putAll(hashKey.toConditionMap());
        if (rangeKey != null) builder.putAll(rangeKey.toConditionMap());
        return builder.build();
    }
}
