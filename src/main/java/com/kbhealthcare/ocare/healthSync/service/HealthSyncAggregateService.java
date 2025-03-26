package com.kbhealthcare.ocare.healthSync.service;

import com.kbhealthcare.ocare.healthSync.dto.aggregate.DailyAggregateMutable;
import com.kbhealthcare.ocare.healthSync.dto.aggregate.DailyHealthSyncEntryAggregate;
import com.kbhealthcare.ocare.healthSync.dto.aggregate.MonthlyAggregateMutable;
import com.kbhealthcare.ocare.healthSync.dto.aggregate.MonthlyHealthSyncEntryAggregate;
import com.kbhealthcare.ocare.healthSync.entity.HealthSyncEntry;
import com.kbhealthcare.ocare.healthSync.repository.HealthSyncEntryRepository;
import kotlin.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HealthSyncAggregateService {
    private final HealthSyncQueryService healthSyncQueryService;
    private final HealthSyncSourceQueryService healthSyncSourceQueryService;
    private final HealthSyncEntryRepository healthSyncEntryRepository;

    /**
     * 특정 레코드키에 대한 일별 건강동기화 데이터 목록을 집계하여 반환합니다.
     */
    public List<DailyHealthSyncEntryAggregate> aggregateDailyHealthSyncDataByRecordKey(String recordKey) {
        Long syncId = healthSyncQueryService.findSyncIdByRecordKey(recordKey);
        Long sourceId = healthSyncSourceQueryService.findSourceIdBySyncId(syncId);

        int offset = 0;
        int size = PAGE_SIZE;

        Map<LocalDate, DailyAggregateMutable> dailyAggregateMap = new HashMap<>();
        DailyAggregateMutable dailyAggreate = null;

        while (true) {
            List<HealthSyncEntry> page =
                    healthSyncEntryRepository.findHealthSyncEntryListBySyncIdOfPage(syncId, sourceId, offset, size);

            for (HealthSyncEntry entry : page) {
                LocalDate dateKey = entry.getPeriodFrom().toLocalDate();

                dailyAggreate = dailyAggregateMap.computeIfAbsent(dateKey, k -> new DailyAggregateMutable(recordKey, dateKey));
                dailyAggreate.addActivity(entry.getActivityValue());
                dailyAggreate.addCalories(entry.getCaloriesValue());
                dailyAggreate.addDistance(entry.getDistanceValue());
            }

            if (page.size() == size) offset = page.get(page.size() - 1).getId().intValue();
            else break;
        }

        if (dailyAggreate == null) return null;

        return dailyAggregateMap
                .values()
                .stream()
                .map(DailyAggregateMutable::toDto)
                .sorted(Comparator.comparing(DailyHealthSyncEntryAggregate::date).reversed()
                        .thenComparing(DailyHealthSyncEntryAggregate::recordKey)
                )
                .toList();
    }

    /**
     * 전체 레코드키에 대한 일별 건강동기화 데이터 목록을 집계하여 반환합니다.
     */
    public List<DailyHealthSyncEntryAggregate> aggregateAllDailyHealthSyncData() {
        int offset = 0;
        int size = PAGE_SIZE;

        Map<Long, String> allSyncIdRecordKeyMap = healthSyncQueryService.findAllSyncIdRecordKeyMap();

        Map<Pair<Long, LocalDate>, DailyAggregateMutable> dailyAggregateMap = new HashMap<>();
        DailyAggregateMutable dailyAggregate;

        while (true) {
            List<HealthSyncEntry> page = healthSyncEntryRepository.findAllHealthSyncEntryListOfPage(offset, size);

            for (HealthSyncEntry entry : page) {
                LocalDate date = entry.getPeriodFrom().toLocalDate();
                Pair<Long, LocalDate> syncIdDateKey = new Pair<>(entry.getSyncId(), date);
                String recordKey = allSyncIdRecordKeyMap.get(entry.getSyncId());

                dailyAggregate = dailyAggregateMap.computeIfAbsent(syncIdDateKey, k -> new DailyAggregateMutable(recordKey, date));
                dailyAggregate.addActivity(entry.getActivityValue());
                dailyAggregate.addCalories(entry.getCaloriesValue());
                dailyAggregate.addDistance(entry.getDistanceValue());
            }

            if (page.size() == size) offset = page.get(page.size() - 1).getId().intValue();
            else break;
        }

        return dailyAggregateMap
                .values()
                .stream()
                .map(DailyAggregateMutable::toDto)
                .sorted(Comparator.comparing(DailyHealthSyncEntryAggregate::date).reversed()
                        .thenComparing(DailyHealthSyncEntryAggregate::recordKey)
                )
                .toList();
    }

    /**
     * 전체 레코드키에 대한 월별 건강동기화 데이터 목록을 집계하여 반환합니다.
     */
    public List<MonthlyHealthSyncEntryAggregate> aggregateAllMonthlyHealthSyncData() {
        int offset = 0;
        int size = PAGE_SIZE;

        Map<Long, String> allSyncIdRecordKeyMap = healthSyncQueryService.findAllSyncIdRecordKeyMap();

        Map<Pair<Long, String>, MonthlyAggregateMutable> monthlyAggregateMap = new HashMap<>();
        MonthlyAggregateMutable monthlyAggregate;

        while (true) {
            List<HealthSyncEntry> page = healthSyncEntryRepository.findAllHealthSyncEntryListOfPage(offset, size);

            for (HealthSyncEntry entry : page) {
                LocalDate date = entry.getPeriodFrom().toLocalDate();
                String yyyyMM = date.getYear() + "-" + date.getMonthValue();
                Pair<Long, String> syncIdDateKey = new Pair<>(entry.getSyncId(), yyyyMM);
                String recordKey = allSyncIdRecordKeyMap.get(entry.getSyncId());

                monthlyAggregate = monthlyAggregateMap.computeIfAbsent(syncIdDateKey, k -> new MonthlyAggregateMutable(recordKey, yyyyMM));
                monthlyAggregate.addActivity(entry.getActivityValue());
                monthlyAggregate.addCalories(entry.getCaloriesValue());
                monthlyAggregate.addDistance(entry.getDistanceValue());
            }

            if (page.size() == size) offset = page.get(page.size() - 1).getId().intValue();
            else break;
        }

        return monthlyAggregateMap
                .values()
                .stream()
                .map(MonthlyAggregateMutable::toDto)
                .sorted(Comparator.comparing(MonthlyHealthSyncEntryAggregate::date).reversed()
                        .thenComparing(MonthlyHealthSyncEntryAggregate::recordKey)
                )
                .toList();
    }

    private static final int PAGE_SIZE = 100;
}
