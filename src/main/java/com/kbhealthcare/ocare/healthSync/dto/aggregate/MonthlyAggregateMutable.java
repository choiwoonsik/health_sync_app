package com.kbhealthcare.ocare.healthSync.dto.aggregate;

import lombok.Getter;

@Getter
public class MonthlyAggregateMutable {
    private final String recordKey;
    private final String date;
    private double activity = 0;
    private double calories = 0;
    private double distance = 0;

    public MonthlyAggregateMutable(String recordKey, String date) {
        this.recordKey = recordKey;
        this.date = date;
    }

    public void addActivity(double value) {
        this.activity += value;
    }

    public void addCalories(double value) {
        this.calories += value;
    }

    public void addDistance(double value) {
        this.distance += value;
    }

    public MonthlyHealthSyncEntryAggregate toDto() {
        return new MonthlyHealthSyncEntryAggregate(recordKey, date, activity, calories, distance);
    }
}