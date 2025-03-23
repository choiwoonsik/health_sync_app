package com.kbhealthcare.ocare.healthSync.dto;

import java.time.OffsetDateTime;

public record PeriodDto(
        OffsetDateTime start,
        OffsetDateTime end
) {
}
