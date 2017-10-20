package com.reactive.dynamodb;

import com.amazonaws.services.dynamodbv2.model.*;
import com.google.common.collect.ImmutableMap;

public class TestData {

    public static final String HASH_KEY = "hashKey";
    public static final String RANGE_KEY = "rangeKey";

    public static final String TEST_TABLE_1_NAME = "testTable1";
    public static final String TEST_TABLE_2_NAME = "testTable2";
    public static String TEST_TABLE_3_NAME = "testTable3";

    public static CreateTableRequest testTable2Schema() {
        return new CreateTableRequest()
                .withTableName(TEST_TABLE_2_NAME)
                .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L))
                .withAttributeDefinitions(new AttributeDefinition(HASH_KEY, ScalarAttributeType.S))
                .withKeySchema(new KeySchemaElement(HASH_KEY, KeyType.HASH));
    }

    public static PutItemRequest testTable2Item() {
        return new PutItemRequest(TEST_TABLE_2_NAME,
                ImmutableMap.of(HASH_KEY, new AttributeValue("testing")));
    }

    public static PutItemRequest testTable1Item1() {
        return new PutItemRequest(TEST_TABLE_1_NAME,
                ImmutableMap.of(HASH_KEY, new AttributeValue("testing"),
                        RANGE_KEY, new AttributeValue("testing1")));
    }

    public static PutItemRequest testTable1Item2() {
        return new PutItemRequest(TEST_TABLE_1_NAME,
                ImmutableMap.of(HASH_KEY, new AttributeValue("testing"),
                        RANGE_KEY, new AttributeValue("testing2")));
    }

    public static CreateTableRequest testTable1Schema() {
        return new CreateTableRequest()
                .withTableName(TEST_TABLE_1_NAME)
                .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L))
                .withAttributeDefinitions(new AttributeDefinition(HASH_KEY, ScalarAttributeType.S),
                        new AttributeDefinition(RANGE_KEY, ScalarAttributeType.S))
                .withKeySchema(new KeySchemaElement(HASH_KEY, KeyType.HASH),
                        new KeySchemaElement(RANGE_KEY, KeyType.RANGE));
    }

    public static CreateTableRequest testTable3Schema() {
        return new CreateTableRequest()
                .withTableName(TEST_TABLE_3_NAME)
                .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L))
                .withAttributeDefinitions(new AttributeDefinition(HASH_KEY, ScalarAttributeType.S))
                .withKeySchema(new KeySchemaElement(HASH_KEY, KeyType.HASH));
    }
}
