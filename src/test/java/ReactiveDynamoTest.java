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
import static java.util.Collections.singletonList;

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

        db.createTable(TestData.testTableSchema());
        db.putItem(TestData.testTableItem());
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
        subscriber.assertValueCount(1);
        subscriber.assertValue(singletonList("testTable"));
    }

    @Test
    public void testGetItemByHashKey() throws Exception {
        ReactiveDynamo reactiveDynamo = new ReactiveDynamo(config);
        TestSubscriber<Map<String, Object>> subscriber = new TestSubscriber<>();
        Observable<Map<String, Object>> observable = reactiveDynamo.itemByHashKey(
                TestData.TEST_TABLE_NAME, TestData.TEST_TABLE_HASH_KEY, "testing");

        observable.subscribe(System.out::print);
        observable.subscribe(subscriber);

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
        subscriber.assertValue(ImmutableMap.of("field1", "testing"));
    }
}