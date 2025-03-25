package com.kbhealthcare.ocare.healthSync.dto.aggregate;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DailyAggregateMutable {
    private final String recordKey;
    private final LocalDate date;
    private double activity = 0;
    private double calories = 0;
    private double distance = 0;

    public DailyAggregateMutable(String recordKey, LocalDate date) {
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

    public DailyHealthSyncEntryAggregate toDto() {
        return new DailyHealthSyncEntryAggregate(recordKey, date, activity, calories, distance);
    }
}