package com.kbhealthcare.ocare.healthSync.dto;

import java.time.OffsetDateTime;

public record HealthSyncDto(
        String recordKey,
        ActivityType type,
        HealthSyncEntryGroupDto data,
        OffsetDateTime lastUpdate
) {
}
