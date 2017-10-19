package com.reactive.dynamodb;

public abstract class DynamoKey {
    private final String name;
    private final String value;

    public DynamoKey(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String name() {
        return name;
    }

    public String value() {
        return value;
    }
}

