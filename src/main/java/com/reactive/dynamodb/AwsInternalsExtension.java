package com.reactive.dynamodb;

import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Collections;
import java.util.Map;

public class AwsInternalsExtension {
    public static Map<String, Object> toSimpleMapValue(Map<String, AttributeValue> item) {
        return item == null ? Collections.emptyMap() : InternalUtils.toSimpleMapValue(item);
    }
}
