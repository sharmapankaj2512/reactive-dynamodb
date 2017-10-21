package com.reactive.dynamodb;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;

import java.util.Map;

class QueryRequestFactory {
    static QueryRequest make(DynamoDbTable table) {
         return new QueryRequest(table.getName()).withKeyConditions(table.toDynamoConditionMap());
    }

    static QueryRequest make(DynamoDbTable table, Map<String, AttributeValue> lastEvaluatedKey) {
        return new QueryRequest(table.getName())
                .withKeyConditions(table.toDynamoConditionMap())
                .withExclusiveStartKey(lastEvaluatedKey);
    }
}
