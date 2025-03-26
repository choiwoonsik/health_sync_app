package com.kbhealthcare.ocare.healthSync.dto.event;

import com.kbhealthcare.ocare.healthSync.dto.HealthSyncEntryDto;

import java.util.List;

public record HealthSyncEvent(
        Long syncId,
        Long sourceId,
        Long RawId,
        List<HealthSyncEntryDto> entries
) {
}
