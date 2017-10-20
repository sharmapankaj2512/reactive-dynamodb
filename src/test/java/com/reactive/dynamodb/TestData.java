package com.reactive.dynamodb;

import com.amazonaws.services.dynamodbv2.model.*;
import com.google.common.collect.ImmutableMap;

public class TestData {

    public static final String TEST_TABLE_1_NAME = "testTable1";
    public static final String TEST_TABLE_1_HASH_KEY = "field1";
    public static final String TEST_TABLE_1_RANGE_KEY = "field2";

    public static final String TEST_TABLE_2_NAME = "testTable2";
    public static final String TEST_TABLE_2_HASH_KEY = "field1";

    public static String TEST_TABLE_3_NAME = "testTable3";

    public static CreateTableRequest testTable2Schema() {
        return new CreateTableRequest()
                .withTableName(TEST_TABLE_2_NAME)
                .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L))
                .withAttributeDefinitions(new AttributeDefinition(TEST_TABLE_2_HASH_KEY, ScalarAttributeType.S))
                .withKeySchema(new KeySchemaElement(TEST_TABLE_2_HASH_KEY, KeyType.HASH));
    }

    public static PutItemRequest testTableItem() {
        return new PutItemRequest(TEST_TABLE_2_NAME,
                ImmutableMap.of(TEST_TABLE_2_HASH_KEY, new AttributeValue("testing")));
    }

    public static PutItemRequest testTable1Item1() {
        return new PutItemRequest(TEST_TABLE_1_NAME,
                ImmutableMap.of(TEST_TABLE_1_HASH_KEY, new AttributeValue("testing"),
                        TEST_TABLE_1_RANGE_KEY, new AttributeValue("testing1")));
    }

    public static PutItemRequest testTable1Item2() {
        return new PutItemRequest(TEST_TABLE_1_NAME,
                ImmutableMap.of(TEST_TABLE_1_HASH_KEY, new AttributeValue("testing"),
                        TEST_TABLE_1_RANGE_KEY, new AttributeValue("testing2")));
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

    public static CreateTableRequest testTable3Schema() {
        return new CreateTableRequest()
                .withTableName(TEST_TABLE_3_NAME)
                .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L))
                .withAttributeDefinitions(new AttributeDefinition(TEST_TABLE_2_HASH_KEY, ScalarAttributeType.S))
                .withKeySchema(new KeySchemaElement(TEST_TABLE_2_HASH_KEY, KeyType.HASH));
    }
}
