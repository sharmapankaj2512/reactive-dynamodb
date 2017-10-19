import com.amazonaws.services.dynamodbv2.model.*;
import com.google.common.collect.ImmutableMap;

public class TestData {

    public static final String TEST_TABLE_NAME = "testTable";
    public static final String TEST_TABLE_HASH_KEY = "field1";

    public static CreateTableRequest testTableSchema() {
        return new CreateTableRequest()
                .withTableName(TEST_TABLE_NAME)
                .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L))
                .withAttributeDefinitions(new AttributeDefinition(TEST_TABLE_HASH_KEY, ScalarAttributeType.S))
                .withKeySchema(new KeySchemaElement(TEST_TABLE_HASH_KEY, KeyType.HASH));
    }

    public static PutItemRequest testTableItem() {
        return new PutItemRequest(TEST_TABLE_NAME,
                ImmutableMap.of(TEST_TABLE_HASH_KEY, new AttributeValue("testing")));
    }
}
