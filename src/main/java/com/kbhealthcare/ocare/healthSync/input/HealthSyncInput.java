package com.kbhealthcare.ocare.healthSync.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record HealthSyncInput(
        @JsonProperty("recordkey")
        String recordKey,
        HealthSyncDataInput data,
        String lastUpdate,
        String type
) {
}

record HealthSyncDataInput(
        List<HealthSyncEntryInput> entries,
        HealthSyncSourceInput source
) {

}

record HealthSyncSourceInput(
        int mode,
        String name,
        String type,
        HealthSyncSourceProductInput product
) {
}

record HealthSyncSourceProductInput(
        String name,
        String vender
) {
}

record HealthSyncEntryInput(
        HealthSyncPeriodInput period,
        HealthSyncUnitValueInput distance,
        HealthSyncUnitValueInput calories,
        int steps
) {
}

record HealthSyncPeriodInput(
        String from,
        String to
) {
}

record HealthSyncUnitValueInput(
        String unit,
        double value
) {
}
