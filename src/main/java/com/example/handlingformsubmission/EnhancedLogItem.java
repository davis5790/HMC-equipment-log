package com.example.handlingformsubmission;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class EnhancedLogItem {
    private String DTS;
    private String HNumber;
    private String name;
    private String device;
    private String date;
    private String time;
    private String SST;
    private String note;

    public EnhancedLogItem() {
    }

    public String getDTS() { return this.DTS; }

    @DynamoDbPartitionKey
    public void setDTS(String DTS) { this.DTS = DTS; }

    public String getHNumber() {
        return this.HNumber;
    }

    public void setHNumber(String HNumber) { this.HNumber = HNumber; }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDevice() { return this.device; }

    public void setDevice(String device) { this.device = device; }

    public String getDate() { return this.date; }

    public void setDate(String date) { this.date = date; }

    public String getTime() { return this.time; }

    public void setTime(String time) { this.time = time; }

    public String getSST() { return this.SST; }

    public void setSST(String SST) { this.SST = SST; }

    public String getNote() { return this.note; }

    public void setNote(String note) { this.note = note; }
}
