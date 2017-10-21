package com.reactive.dynamodb;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
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

import java.util.List;
import java.util.Map;

import static com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import static com.google.common.collect.ImmutableMap.of;
import static com.reactive.dynamodb.TestData.*;
import static java.util.Arrays.asList;

public class ReactiveDynamoTest {
    private static final String region = "foobar";
    private static final String endpoint = "http://localhost:8000";

    private static DynamoDBProxyServer server;
    private static EndpointConfiguration config = new EndpointConfiguration(endpoint, region);
    private final AWSStaticCredentialsProvider fake = new AWSStaticCredentialsProvider(new BasicAWSCredentials("foo", "bar"));

    @BeforeClass
    public static void setup() throws Exception {
        final String[] localArgs = {"-inMemory"};
        server = ServerRunner.createServerFromCommandLineArgs(localArgs);
        server.start();

        AmazonDynamoDB db = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(config)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("foo", "bar")))
                .build();

        db.createTable(testTable1Schema());
        db.putItem(testTable1Item1());
        db.putItem(testTable1Item2());

        db.createTable(testTable2Schema());
        db.putItem(testTable2Item());

        db.createTable(testTable3Schema());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (server != null) server.stop();
    }

    @Test
    public void testListTables() throws Exception {
        Observable<List<String>> observable = new ReactiveDynamo(fake, config).tables();
        TestSubscriber<List<String>> subscriber = new TestSubscriber<>();

        observable.subscribe(System.out::print);
        observable.subscribe(subscriber);

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValue(asList("testTable1", "testTable2", "testTable3"));
    }

    @Test
    public void testGetItemByHashKey() throws Exception {
        ReactiveDynamo reactiveDynamo = new ReactiveDynamo(fake, config);
        TestSubscriber<Map<String, Object>> subscriber = new TestSubscriber<>();
        DynamoDbHashKey hashKey = new DynamoDbHashKey(HASH_KEY, "testing");
        DynamoDbTable table = new DynamoDbTable(TEST_TABLE_2_NAME, hashKey);
        Observable<Map<String, Object>> observable = reactiveDynamo.item(table);

        observable.subscribe(System.out::print);
        observable.subscribe(subscriber);

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValue(of(HASH_KEY, "testing"));
    }

    @Test
    public void testGetItemByCompositeKey() throws Exception {
        ReactiveDynamo reactiveDynamo = new ReactiveDynamo(fake, config);
        TestSubscriber<Map<String, Object>> subscriber = new TestSubscriber<>();
        DynamoDbHashKey hashKey = new DynamoDbHashKey(HASH_KEY, "testing");
        DynamoDbHashKey rangeKey = new DynamoDbHashKey(RANGE_KEY, "testing1");
        DynamoDbTable table = new DynamoDbTable(TEST_TABLE_1_NAME, hashKey, rangeKey);
        Observable<Map<String, Object>> observable = reactiveDynamo.item(table);

        observable.subscribe(System.out::print);
        observable.subscribe(subscriber);

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValue(of(HASH_KEY, "testing", RANGE_KEY, "testing1"));
    }

    @Test
    public void testAddItemByHashKey() {
        ReactiveDynamo reactiveDynamo = new ReactiveDynamo(fake, config);
        TestSubscriber<Map<String, Object>> subscriber = new TestSubscriber<>();

        ImmutableMap<String, Object> data = of(HASH_KEY, "testing");
        Observable<Map<String, Object>> observable = reactiveDynamo.add(TEST_TABLE_3_NAME, data);
        observable.subscribe(subscriber);
        observable.subscribe(System.out::println);

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValue(of());
    }

    @Test
    public void testGetItemsByHashKey() throws Exception {
        ReactiveDynamo reactiveDynamo = new ReactiveDynamo(fake, config);
        TestSubscriber<List<Map<String, Object>>> subscriber = new TestSubscriber<>();
        DynamoDbHashKey hashKey = new DynamoDbHashKey(HASH_KEY, "testing");
        DynamoDbTable table = new DynamoDbTable(TEST_TABLE_1_NAME, hashKey);
        Observable<List<Map<String, Object>>> observable = reactiveDynamo.items(table);

        observable.subscribe(System.out::print);
        observable.subscribe(subscriber);

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValue(
                asList(of(HASH_KEY, "testing", RANGE_KEY, "testing1"),
                        of(HASH_KEY, "testing", RANGE_KEY, "testing2")));
    }
}