package com.kbhealthcare.ocare.healthSync.dto.aggregate;

import com.kbhealthcare.ocare.healthSync.response.HealthSyncResponse;

import java.time.LocalDate;

public record DailyHealthSyncEntryAggregate(
        String recordKey,
        LocalDate date,
        Double activityValue,
        Double calorieValue,
        Double distanceValue
) {
    public HealthSyncResponse toDto() {
        String yyyyMMdd = date.getYear() + "-" + date.getMonthValue() + "." + date.getDayOfMonth();

        return new HealthSyncResponse(recordKey, yyyyMMdd, activityValue, calorieValue, distanceValue);
    }
}
