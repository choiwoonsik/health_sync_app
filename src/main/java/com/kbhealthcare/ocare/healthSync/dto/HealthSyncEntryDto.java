package com.kbhealthcare.ocare.healthSync.dto;

import com.kbhealthcare.ocare.healthSync.entity.HealthSyncEntry;

public record HealthSyncEntryDto(
        PeriodDto period,
        UnitValueDto distance,
        UnitValueDto calories,
        Double steps
) {
    public HealthSyncEntry toEntity(
            Long syncId,
            Long sourceId,
            ActivityType activityType
    ) {
        return HealthSyncEntry.builder()
                .syncId(syncId)
                .sourceId(sourceId)
                .periodFrom(period.start())
                .periodTo(period.end())
                .distanceValue(distance.value())
                .distanceUnit(distance.unit())
                .caloriesValue(calories.value())
                .caloriesUnit(calories.unit())
                .activityValue(steps)
                .activityType(activityType)
                .sourceId(sourceId)
                .build();
    }
}