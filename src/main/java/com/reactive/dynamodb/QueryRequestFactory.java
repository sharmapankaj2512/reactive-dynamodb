package com.reactive.dynamodb;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;

import java.util.Map;

public class QueryRequestFactory {
    public static QueryRequest make(DynamoDbTable table) {
         return new QueryRequest(table.getName()).withKeyConditions(table.toDynamoConditionMap());
    }

    public static QueryRequest make(DynamoDbTable table, Map<String, AttributeValue> lastEvaluatedKey) {
        return new QueryRequest(table.getName())
                .withKeyConditions(table.toDynamoConditionMap())
                .withExclusiveStartKey(lastEvaluatedKey);
    }
}
