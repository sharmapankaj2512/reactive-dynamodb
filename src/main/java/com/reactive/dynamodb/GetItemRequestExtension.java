package com.reactive.dynamodb;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.google.common.collect.ImmutableMap;

public class GetItemRequestExtension {
    public static GetItemRequest from(DynamoDbTable table) {
        ImmutableMap<String, AttributeValue> map = table.toDynamoKeyMap();
        return new GetItemRequest().withTableName(table.getName()).withKey(map);
    }
}
