package com.kbhealthcare.ocare.healthSync.dto;

import java.util.List;

public record HealthSyncEntryGroupDto(
        HealthSyncSourceDto source,
        List<HealthSyncEntryDto> entries
) {
}
