package com.example.handlingformsubmission;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;

import static software.amazon.awssdk.enhanced.dynamodb.internal.AttributeValues.stringValue;

@Component("DynamoDBEnhanced")
public class DynamoDBEnhanced {

    private DynamoDbClient getClient() {
        Region region = Region.US_EAST_2;
        return DynamoDbClient.builder()
                .region(region)
                .build();
    }

    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    LocalDateTime now = LocalDateTime.now();

    public void injectDynamoItem(EnhancedLogItem item){

        try {
            DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(getClient())
                    .build();

            DynamoDbTable<EnhancedLogItem> mappedTable = enhancedClient.table("Equipment-Log-Time2", TableSchema.fromBean(EnhancedLogItem.class));

            PutItemEnhancedRequest<EnhancedLogItem> enReq = PutItemEnhancedRequest.builder(EnhancedLogItem.class)
                    .item(item)
                    .build();

            mappedTable.putItem(enReq);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public List<EnhancedLogItem> getAllItems(SearchItem search) {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(getClient())
                .build();

        try{
            DynamoDbTable<EnhancedLogItem> table = enhancedClient.table("Equipment-Log-Time2", TableSchema.fromBean(EnhancedLogItem.class));
            Iterator<EnhancedLogItem> results = table.scan().items().iterator();
            EnhancedLogItem workItem ;
            List<EnhancedLogItem> itemList = new ArrayList<>();


            while (results.hasNext()) {
                EnhancedLogItem work = results.next();
                LocalDateTime LocalDT = LocalDateTime.parse(work.getDTS(), formatter);
                if (search.getTimePeriodDays() != null){
                    if (LocalDT.isAfter(now.minusDays(search.getTimePeriodDays()))) {
                        itemList.add(work);
                    }
                } else {
                    itemList.add(work);
                }
            }
            Collections.sort(itemList, Comparator.comparing(EnhancedLogItem::getDevice)
                    .thenComparing(EnhancedLogItem::getHNumber)
                    .thenComparing(EnhancedLogItem::getDTS).reversed());

            return itemList;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public List<EnhancedLogItem> getItemsByDevice(SearchItem search) {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(getClient())
                .build();

        try{
            DynamoDbTable<EnhancedLogItem> table = enhancedClient.table("Equipment-Log-Time2", TableSchema.fromBean(EnhancedLogItem.class));

            ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                    .attributesToProject("HNumber", "device", "date", "time", "name", "DTS")
                    .filterExpression(Expression.builder()
                            .expression("device = :device")
                            .expressionValues(
                                    Map.of(":device", stringValue(search.getDevice())))
                            .build())
                    .build();

            Iterator<EnhancedLogItem> results = table.scan(request).items().iterator();
            List<EnhancedLogItem> itemList = new ArrayList<>();

            while (results.hasNext()) {
                EnhancedLogItem work = results.next();
                LocalDateTime LocalDT = LocalDateTime.parse(work.getDTS(), formatter);
                if (search.getTimePeriodDays() != null){
                    if (LocalDT.isAfter(now.minusDays(search.getTimePeriodDays()))) {
                        itemList.add(work);
                    }
                } else {
                    itemList.add(work);
                }
            }
            Collections.sort(itemList, Comparator.comparing(EnhancedLogItem::getHNumber)
                    .thenComparing(EnhancedLogItem::getDTS).reversed());

            return itemList;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public List<EnhancedLogItem> scanFilterHNumber(SearchItem search) {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(getClient())
                .build();

        try {
            DynamoDbTable<EnhancedLogItem> table = enhancedClient.table("Equipment-Log-Time2", TableSchema.fromBean(EnhancedLogItem.class));

            ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                    .attributesToProject("HNumber", "device", "date", "time", "name", "DTS")
                    .filterExpression(Expression.builder()
                            .expression("HNumber = :HNumber")
                            .expressionValues(
                                    Map.of(":HNumber", stringValue(search.getHNumber().toUpperCase())))
                            .build())
                    .build();

            Iterator<EnhancedLogItem> results = table.scan(request).items().iterator();
            List<EnhancedLogItem> itemList = new ArrayList<>();

            while (results.hasNext()) {
                EnhancedLogItem work = results.next();
                LocalDateTime LocalDT = LocalDateTime.parse(work.getDTS(), formatter);
                if (search.getTimePeriodDays() != null){
                    if (LocalDT.isAfter(now.minusDays(search.getTimePeriodDays()))) {
                        itemList.add(work);
                    }
                } else {
                    itemList.add(work);
                }
            }
            Collections.sort(itemList, Comparator.comparing(EnhancedLogItem::getDTS).reversed());
            return itemList;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public HashMap<String, Integer> usageReport(EnhancedLogItem workItem) {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(getClient())
                .build();

        try {
            HashMap<String, Integer> usageReport = new HashMap<>();
//            while (items)
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

}