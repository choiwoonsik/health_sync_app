package com.kbhealthcare.ocare.healthSync.dto.aggregate;

import com.kbhealthcare.ocare.healthSync.response.HealthSyncResponse;

public record MonthlyHealthSyncEntryAggregate(
        String recordKey,
        String date,
        Double activityValue,
        Double calorieValue,
        Double distanceValue
) {
    public HealthSyncResponse toDto() {
        return new HealthSyncResponse(recordKey, date, activityValue, calorieValue, distanceValue);
    }
}
