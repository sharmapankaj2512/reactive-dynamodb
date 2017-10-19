package com.reactive.dynamodb;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DynamoDbTable {
    private String name;
    private DynamoDbHashKey dynamoDbHashKey;

    public ImmutableMap<String, AttributeValue> toDynamoKeyMap() {
        return ImmutableMap.of(
                dynamoDbHashKey.getName(),
                new AttributeValue(dynamoDbHashKey.getValue()));
    }
}
