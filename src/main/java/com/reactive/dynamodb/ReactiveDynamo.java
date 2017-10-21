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
import static com.reactive.dynamodb.GetItemRequestExtension.from;

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
        return Observable.from(db.getItemAsync(from(table)))
                .map(GetItemResult::getItem)
                .map(AwsInternalsExtension::toSimpleMapValue);
    }

    Observable<Map<String, Object>> add(String tableName, Map<String, Object> data) {
        return Observable.from(db.putItemAsync(PutItemRequestExtension.from(tableName, data)))
                .map(PutItemResult::getAttributes)
                .map(AwsInternalsExtension::toSimpleMapValue);
    }

    Observable<List<Map<String, Object>>> items(DynamoDbTable table) {
        Observable<QueryResult> observable = Observable.from(db.queryAsync(QueryRequestExtension.from(table)));
        return observable.mergeWith(observable.flatMap((QueryResult r) -> items(table, r.getLastEvaluatedKey())))
                .map(QueryResult::getItems)
                .map(items -> items.stream()
                        .map(AwsInternalsExtension::toSimpleMapValue)
                        .collect(Collectors.toList()));
    }

    private Observable<QueryResult> items(DynamoDbTable table, Map<String, AttributeValue> lastEvaluatedKey) {
        if (lastEvaluatedKey == null || lastEvaluatedKey.size() == 0) return Observable.empty();
        QueryRequest queryRequest = QueryRequestExtension.from(table, lastEvaluatedKey);
        Observable<QueryResult> observable = Observable.from(db.queryAsync(queryRequest));
        return observable.mergeWith(observable.flatMap((QueryResult r) -> items(table, r.getLastEvaluatedKey())));
    }
}
