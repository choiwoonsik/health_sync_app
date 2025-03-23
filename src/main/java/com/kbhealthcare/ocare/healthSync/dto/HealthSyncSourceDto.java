package com.kbhealthcare.ocare.healthSync.dto;

public record HealthSyncSourceDto(
        int mode,
        String name,
        String type,
        DeviceDto product
) {
}