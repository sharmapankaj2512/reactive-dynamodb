package com.reactive.dynamodb;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;
import rx.Observable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import static com.reactive.dynamodb.GetItemRequestFactory.make;

class ReactiveDynamo {
    private final AmazonDynamoDBAsync db;

    ReactiveDynamo(AWSCredentialsProvider awsCredentialsProvider, EndpointConfiguration endpointConfiguration) {
        this.db = AmazonDynamoDBAsyncClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(awsCredentialsProvider)
                .build();
    }

    Observable<List<String>> tables() {
        return Observable.from(db.listTablesAsync())
                .map(ListTablesResult::getTableNames);
    }

    Observable<Map<String, Object>> item(DynamoDbTable table) {
        return Observable.from(db.getItemAsync(make(table)))
                .map(GetItemResult::getItem)
                .map(AwsInternalsExtension::toSimpleMapValue);
    }

    Observable<Map<String, Object>> add(String tableName, Map<String, Object> data) {
        return Observable.from(db.putItemAsync(PutItemRequestFactory.make(tableName, data)))
                .map(PutItemResult::getAttributes)
                .map(AwsInternalsExtension::toSimpleMapValue);
    }

    Observable<List<Map<String, Object>>> items(DynamoDbTable table) {
        return items(table, QueryRequestFactory.make(table))
                .map(QueryResult::getItems)
                .map(items -> items.stream()
                        .map(AwsInternalsExtension::toSimpleMapValue)
                        .collect(Collectors.toList()));
    }

    private Observable<QueryResult> items(DynamoDbTable table, Map<String, AttributeValue> lastEvaluatedKey) {
        if (lastEvaluatedKey == null) return Observable.empty();
        return items(table, QueryRequestFactory.make(table, lastEvaluatedKey));
    }

    private Observable<QueryResult> items(DynamoDbTable table, QueryRequest queryRequest) {
        Observable<QueryResult> head = Observable.from(db.queryAsync(queryRequest));
        Observable<QueryResult> tail = head.flatMap((QueryResult r) -> items(table, r.getLastEvaluatedKey()));
        return head.mergeWith(tail);
    }
}
