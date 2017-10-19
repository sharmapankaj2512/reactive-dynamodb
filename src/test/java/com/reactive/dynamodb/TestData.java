package com.reactive.dynamodb;

import com.amazonaws.services.dynamodbv2.model.*;
import com.google.common.collect.ImmutableMap;

public class TestData {

    public static final String TABLE_WITH_HASH_KEY = "testTable";
    public static final String TEST_TABLE_HASH_KEY = "field1";

    public static final String TEST_TABLE_1_NAME = "testTable1";
    public static final String TEST_TABLE_1_HASH_KEY = "field1";
    public static final String TEST_TABLE_1_RANGE_KEY = "field2";

    public static CreateTableRequest testTableSchema() {
        return new CreateTableRequest()
                .withTableName(TABLE_WITH_HASH_KEY)
                .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L))
                .withAttributeDefinitions(new AttributeDefinition(TEST_TABLE_HASH_KEY, ScalarAttributeType.S))
                .withKeySchema(new KeySchemaElement(TEST_TABLE_HASH_KEY, KeyType.HASH));
    }

    public static PutItemRequest testTableItem() {
        return new PutItemRequest(TABLE_WITH_HASH_KEY,
                ImmutableMap.of(TEST_TABLE_HASH_KEY, new AttributeValue("testing")));
    }

    public static PutItemRequest testTable1Item() {
        return new PutItemRequest(TEST_TABLE_1_NAME,
                ImmutableMap.of(TEST_TABLE_1_HASH_KEY, new AttributeValue("testing"),
                        TEST_TABLE_1_RANGE_KEY, new AttributeValue("testing")));
    }

    public static CreateTableRequest testTable1Schema() {
        return new CreateTableRequest()
                .withTableName(TEST_TABLE_1_NAME)
                .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L))
                .withAttributeDefinitions(new AttributeDefinition(TEST_TABLE_1_HASH_KEY, ScalarAttributeType.S),
                        new AttributeDefinition(TEST_TABLE_1_RANGE_KEY, ScalarAttributeType.S))
                .withKeySchema(new KeySchemaElement(TEST_TABLE_1_HASH_KEY, KeyType.HASH),
                        new KeySchemaElement(TEST_TABLE_1_RANGE_KEY, KeyType.RANGE));
    }
}
