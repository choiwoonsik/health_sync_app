package com.kbhealthcare.ocare.healthSync.dto;

import com.kbhealthcare.ocare.healthSync.entity.HealthSync;

import java.time.OffsetDateTime;

public record HealthSyncDto(
        String recordKey,
        ActivityType type,
        HealthSyncEntryGroupDto data,
        OffsetDateTime lastUpdate
) {
    public HealthSync toEntity() {
        return HealthSync.builder()
                .recordKey(recordKey)
                .healthSyncType(type)
                .build();
    }
}
