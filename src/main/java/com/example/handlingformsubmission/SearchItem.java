package com.example.handlingformsubmission;

public class SearchItem {
    private String HNumber;
    private String device;
    private Integer timePeriodDays;
    private String email;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getHNumber() { return HNumber; }

    public void setHNumber(String HNumber) {
        this.HNumber = HNumber;
    }

    public Integer getTimePeriodDays() {
        return timePeriodDays;
    }

    public void setTimePeriodDays(Integer timePeriodDays) {
        this.timePeriodDays = timePeriodDays;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}
