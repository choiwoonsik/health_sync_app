package com.kbhealthcare.ocare.healthSync.dto;

import java.util.List;

public record HealthSyncEntryChunkMessageDto(
        Long syncId,
        Long sourceId,
        int chunkIndex,
        int totalChunks,
        List<HealthSyncEntryDto> entries
) {
}
