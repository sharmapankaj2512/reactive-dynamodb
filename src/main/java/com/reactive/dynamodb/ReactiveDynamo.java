package com.reactive.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.model.*;
import rx.Observable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import static com.reactive.dynamodb.GetItemRequestExtension.from;

class ReactiveDynamo {
    private final AmazonDynamoDBAsync db;

    ReactiveDynamo(EndpointConfiguration endpointConfiguration) {
        this.db = AmazonDynamoDBAsyncClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .build();
    }

    Observable<List<String>> tables() {
        return Observable.from(db.listTablesAsync())
                .map(ListTablesResult::getTableNames);
    }

    Observable<Map<String, Object>> item(DynamoDbTable table) {
        return Observable.from(db.getItemAsync(from(table)))
                .map(GetItemResult::getItem)
                .map(this::toSimpleMap);
    }

    Observable<Map<String, Object>> add(String tableName, Map<String, Object> data) {
        return Observable.from(db.putItemAsync(PutItemRequestExtension.from(tableName, data)))
                .map(PutItemResult::getAttributes)
                .map(this::toSimpleMap);
    }

    private Map<String, Object> toSimpleMap(Map<String, AttributeValue> item) {
        return item == null ? Collections.emptyMap() : InternalUtils.toSimpleMapValue(item);
    }
}
