package com.kbhealthcare.ocare.healthSync.dto;

public record HealthSyncEntryDto(
        PeriodDto period,
        UnitValueDto distance,
        UnitValueDto calories,
        int steps
) {
}