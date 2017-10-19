import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.google.common.collect.ImmutableMap;
import rx.Observable;

import java.util.List;
import java.util.Map;

import static com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;

class ReactiveDynamo {
    private final AmazonDynamoDBAsync db;

    ReactiveDynamo(EndpointConfiguration endpointConfiguration) {
        this.db = AmazonDynamoDBAsyncClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .build();
    }

    Observable<List<String>> tables() {
        return Observable.from(db.listTablesAsync())
                .map(ListTablesResult::getTableNames);
    }

    Observable<Map<String, Object>> itemByHashKey(String tableName, String hashKeyName, String hashKeyValue) {
        ImmutableMap<String, AttributeValue> map = ImmutableMap.of(hashKeyName, new AttributeValue(hashKeyValue));
        GetItemRequest getItemRequest = new GetItemRequest().withTableName(tableName).withKey(map);

        return Observable.from(db.getItemAsync(getItemRequest))
                .map(GetItemResult::getItem)
                .map(InternalUtils::toSimpleMapValue);
    }
}
