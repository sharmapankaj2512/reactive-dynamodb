package com.reactive.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.google.common.collect.ImmutableMap;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import static com.reactive.dynamodb.TestData.*;

public class ReactiveDynamoTest {
    private static final String region = "foobar";
    private static final String endpoint = "http://localhost:8000";

    private static DynamoDBProxyServer server;
    private static EndpointConfiguration config = new EndpointConfiguration(endpoint, region);

    @BeforeClass
    public static void setup() throws Exception {
        final String[] localArgs = {"-inMemory"};
        server = ServerRunner.createServerFromCommandLineArgs(localArgs);
        server.start();

        AmazonDynamoDB db = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(config)
                .build();

        db.createTable(testTable1Schema());
        db.putItem(testTable1Item());

        db.createTable(testTable2Schema());
        db.putItem(testTableItem());

        db.createTable(testTable3Schema());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (server != null) server.stop();
    }

    @Test
    public void testListTables() throws Exception {
        Observable<List<String>> observable = new ReactiveDynamo(config).tables();
        TestSubscriber<List<String>> subscriber = new TestSubscriber<>();

        observable.subscribe(System.out::print);
        observable.subscribe(subscriber);

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValue(Arrays.asList("testTable1", "testTable2", "testTable3"));
    }

    @Test
    public void testGetItemByHashKey() throws Exception {
        ReactiveDynamo reactiveDynamo = new ReactiveDynamo(config);
        TestSubscriber<Map<String, Object>> subscriber = new TestSubscriber<>();
        DynamoDbHashKey hashKey = new DynamoDbHashKey(TEST_TABLE_2_HASH_KEY, "testing");
        DynamoDbTable table = new DynamoDbTable(TEST_TABLE_2_NAME, hashKey);
        Observable<Map<String, Object>> observable = reactiveDynamo.item(table);

        observable.subscribe(System.out::print);
        observable.subscribe(subscriber);

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValue(ImmutableMap.of("field1", "testing"));
    }

    @Test
    public void testGetItemByCompositeKey() throws Exception {
        ReactiveDynamo reactiveDynamo = new ReactiveDynamo(config);
        TestSubscriber<Map<String, Object>> subscriber = new TestSubscriber<>();
        DynamoDbHashKey hashKey = new DynamoDbHashKey(TEST_TABLE_1_HASH_KEY, "testing");
        DynamoDbHashKey rangeKey = new DynamoDbHashKey(TEST_TABLE_1_RANGE_KEY, "testing");
        DynamoDbTable table = new DynamoDbTable(TEST_TABLE_1_NAME, hashKey, rangeKey);
        Observable<Map<String, Object>> observable = reactiveDynamo.item(table);

        observable.subscribe(System.out::print);
        observable.subscribe(subscriber);

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValue(ImmutableMap.of("field1", "testing", "field2", "testing"));
    }

    @Test
    public void testAddItemByHashKey() {
        ReactiveDynamo reactiveDynamo = new ReactiveDynamo(config);
        TestSubscriber<Map<String, Object>> subscriber = new TestSubscriber<>();

        ImmutableMap<String, Object> data = ImmutableMap.of("field1", "testing");
        Observable<Map<String, Object>> observable = reactiveDynamo.add(TestData.TEST_TABLE_3_NAME, data);
        observable.subscribe(subscriber);
        observable.subscribe(System.out::println);

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValue(ImmutableMap.of());
    }
}