package com.reactive.dynamodb;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DynamoDbHashKey {
    private String name;
    private String value;
}
