package com.kbhealthcare.ocare.healthSync.dto;

import java.util.List;

public record HealthSyncEntryChunkMessageDto(
        Long syncId,
        Long sourceId,
        Long rawId,
        int chunkIndex,
        int totalChunks,
        List<HealthSyncEntryDto> entries
) {
}
