package com.kbhealthcare.ocare.healthSync.service;

import com.kbhealthcare.ocare.healthSync.dto.aggregate.DailyHealthSyncEntryAggregate;
import com.kbhealthcare.ocare.healthSync.dto.aggregate.MonthlyHealthSyncEntryAggregate;
import com.kbhealthcare.ocare.healthSync.response.HealthSyncResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthSyncService {
    private final HealthSyncAggregateService healthSyncAggregateService;

    public List<HealthSyncResponse> getDailyHealthSyncByRecordKey(String recordKey) {
        List<DailyHealthSyncEntryAggregate> dailyHealthSyncEntryAggregates =
                healthSyncAggregateService.aggregateDailyHealthSyncDataByRecordKey(recordKey);

        return dailyHealthSyncEntryAggregates
                .stream()
                .map(DailyHealthSyncEntryAggregate::toResponse)
                .toList();
    }

    public List<HealthSyncResponse> getAllDailyHealthSync() {
        List<DailyHealthSyncEntryAggregate> dailyHealthSyncEntryAggregates =
                healthSyncAggregateService.aggregateAllDailyHealthSyncData();

        return dailyHealthSyncEntryAggregates
                .stream()
                .map(DailyHealthSyncEntryAggregate::toResponse)
                .toList();
    }

    public List<HealthSyncResponse> getAllMonthlyHealthSync() {
        List<MonthlyHealthSyncEntryAggregate> monthlyHealthSyncEntryAggregates =
                healthSyncAggregateService.aggregateAllMonthlyHealthSyncData();

        return monthlyHealthSyncEntryAggregates
                .stream()
                .map(MonthlyHealthSyncEntryAggregate::toDto)
                .toList();
    }
}
