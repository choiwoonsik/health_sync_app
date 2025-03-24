package com.kbhealthcare.ocare.healthSync.dto;

import com.kbhealthcare.ocare.healthSync.entity.HealthSyncSource;

public record HealthSyncSourceDto(
        int mode,
        String name,
        String type,
        DeviceDto product
) {
    public HealthSyncSource toEntity(Long syncId) {
        return HealthSyncSource.builder()
                .syncId(syncId)
                .deviceName(product.name())
                .deviceVender(product.vender())
                .sourceMode(mode)
                .sourceName(name)
                .sourceType(type)
                .build();
    }
}