package com.kbhealthcare.ocare.healthSync.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kbhealthcare.ocare.healthSync.dto.ActivityType;
import com.kbhealthcare.ocare.healthSync.dto.DeviceDto;
import com.kbhealthcare.ocare.healthSync.dto.HealthSyncDto;
import com.kbhealthcare.ocare.healthSync.dto.HealthSyncEntryDto;
import com.kbhealthcare.ocare.healthSync.dto.HealthSyncEntryGroupDto;
import com.kbhealthcare.ocare.healthSync.dto.HealthSyncSourceDto;
import com.kbhealthcare.ocare.healthSync.dto.PeriodDto;
import com.kbhealthcare.ocare.healthSync.dto.Unit;
import com.kbhealthcare.ocare.healthSync.dto.UnitValueDto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.kbhealthcare.ocare.healthSync.common.DateFormatter.yyyyMMddTHHmmssZ;
import static com.kbhealthcare.ocare.healthSync.common.DateFormatter.yyyyMMdd_HHmmss;
import static com.kbhealthcare.ocare.healthSync.common.DateFormatter.yyyyMMdd_HHmmss_Z;

public record HealthSyncInput(
        @JsonProperty("recordkey")
        String recordKey,
        HealthSyncDataInput data,
        String lastUpdate,
        String type
) {

    public HealthSyncDto toDto() {
        return new HealthSyncDto(
                recordKey,
                ActivityType.find(type),
                data.toEntryGroupDto(),
                OffsetDateTime.parse(lastUpdate, yyyyMMdd_HHmmss_Z)
        );
    }
}

record HealthSyncDataInput(
        List<HealthSyncEntryInput> entries,
        HealthSyncSourceInput source
) {
    public HealthSyncEntryGroupDto toEntryGroupDto() {
        return new HealthSyncEntryGroupDto(
                source.toSourceDto(),
                entries.stream()
                        .map(entry -> entry.toEntryDto(source.mode() == 10))
                        .toList()
        );
    }
}

record HealthSyncSourceInput(
        int mode,
        String name,
        String type,
        HealthSyncSourceProductInput product
) {
    public HealthSyncSourceDto toSourceDto() {
        return new HealthSyncSourceDto(
                mode,
                name,
                type,
                product.toProduct()
        );
    }
}

record HealthSyncSourceProductInput(
        String name,
        String vender
) {
    public DeviceDto toProduct() {
        return new DeviceDto(
                name,
                vender
        );
    }
}

record HealthSyncEntryInput(
        HealthSyncPeriodInput period,
        HealthSyncUnitValueInput distance,
        HealthSyncUnitValueInput calories,
        Double steps
) {
    public HealthSyncEntryDto toEntryDto(Boolean withZone) {
        return new HealthSyncEntryDto(
                period.toPeriodDto(withZone),
                distance.toUnitValueDto(),
                calories.toUnitValueDto(),
                steps
        );
    }
}

record HealthSyncPeriodInput(
        String from,
        String to
) {
    public PeriodDto toPeriodDto(Boolean withZone) {
        ZoneOffset OFFSET_SEOUL = ZoneOffset.of("+0900");

        OffsetDateTime fromOffsetTime;
        OffsetDateTime toOffsetTime;

        if (!withZone) {
            fromOffsetTime = LocalDateTime.parse(from, yyyyMMdd_HHmmss).atOffset(OFFSET_SEOUL);
            toOffsetTime = LocalDateTime.parse(to, yyyyMMdd_HHmmss).atOffset(OFFSET_SEOUL);
        } else {
            fromOffsetTime = OffsetDateTime.parse(from, yyyyMMddTHHmmssZ);
            toOffsetTime = OffsetDateTime.parse(to, yyyyMMddTHHmmssZ);
        }

        return new PeriodDto(
                fromOffsetTime,
                toOffsetTime
        );
    }
}

record HealthSyncUnitValueInput(
        String unit,
        double value
) {
    public UnitValueDto toUnitValueDto() {
        Unit unitOrNull = Unit.findOrNull(unit);

        if (unitOrNull == null) {
            throw new IllegalArgumentException("Invalid unit: " + unit);
        }

        return new UnitValueDto(
                unitOrNull,
                value
        );
    }
}

