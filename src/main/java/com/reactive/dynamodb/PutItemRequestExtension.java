package com.reactive.dynamodb;

import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import java.util.Map;

public class PutItemRequestExtension {
    public static PutItemRequest from(String tableName, Map<String, Object> data) {
        return new PutItemRequest(tableName, InternalUtils.fromSimpleMap(data))
                .withReturnValues(ReturnValue.ALL_OLD);
    }
}
