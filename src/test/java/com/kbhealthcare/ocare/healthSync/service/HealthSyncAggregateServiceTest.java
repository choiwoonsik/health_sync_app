package com.kbhealthcare.ocare.healthSync.service;

import com.kbhealthcare.ocare.healthSync.dto.ActivityType;
import com.kbhealthcare.ocare.healthSync.dto.Unit;
import com.kbhealthcare.ocare.healthSync.dto.aggregate.DailyHealthSyncEntryAggregate;
import com.kbhealthcare.ocare.healthSync.dto.aggregate.MonthlyHealthSyncEntryAggregate;
import com.kbhealthcare.ocare.healthSync.entity.HealthSyncEntry;
import com.kbhealthcare.ocare.healthSync.repository.HealthSyncEntryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HealthSyncAggregateServiceTest {
    @Mock
    private HealthSyncQueryService healthSyncQueryService;

    @Mock
    private HealthSyncSourceQueryService healthSyncSourceQueryService;

    @Mock
    private HealthSyncEntryRepository healthSyncEntryRepository;

    @InjectMocks
    private HealthSyncAggregateService sut;

    private HealthSyncEntry createHealthSyncEntry(
            Long id,
            Long syncId,
            Long sourceId,
            OffsetDateTime periodFrom,
            OffsetDateTime periodTo,
            double activityValue,
            double caloriesValue,
            double distanceValue
    ) {
        return HealthSyncEntry.builder()
                .id(id)
                .syncId(syncId)
                .sourceId(sourceId)
                .periodFrom(periodFrom)
                .periodTo(periodTo)
                .activityValue(activityValue)
                .caloriesValue(caloriesValue)
                .distanceValue(distanceValue)
                .distanceUnit(Unit.KM)
                .caloriesUnit(Unit.KCAL)
                .activityType(ActivityType.STEPS)
                .build();
    }

    @Test
    @DisplayName("`aggregateDailyHealthSyncDataByRecordKey` - 특정 레코드에 대한 일별 건강 동기화 데이터 집계 테스트, 전달 받은 레코드키를 기준으로 일일 단위로 집계가 되어야 한다.")
    public void testAggregateDailyHealthSyncDataByRecordKey() {
        // given
        String recordKey = "record-1";
        Long syncId = 1L;
        Long sourceId = 10L;

        when(healthSyncQueryService.findSyncIdByRecordKey(recordKey)).thenReturn(syncId);
        when(healthSyncSourceQueryService.findSourceIdBySyncId(syncId)).thenReturn(sourceId);

        // 엔트리 생성: 같은 날짜
        LocalDate today = LocalDate.of(2025, 3, 25);
        OffsetDateTime periodFrom = OffsetDateTime.of(today.atStartOfDay(), ZoneOffset.UTC);
        OffsetDateTime periodTo = periodFrom.plusHours(1);

        HealthSyncEntry todayEntry1 = createHealthSyncEntry(1L, syncId, sourceId, periodFrom, periodTo, 100.0, 50.0, 2.0);
        HealthSyncEntry todayEntity2 = createHealthSyncEntry(2L, syncId, sourceId, periodFrom, periodTo, 200.0, 100.0, 3.0);

        // 엔트리 생성: 다른 날짜
        LocalDate tomorrow = LocalDate.of(2025, 3, 26);
        OffsetDateTime tomorrowPeriodFrom = OffsetDateTime.of(tomorrow.atStartOfDay(), ZoneOffset.UTC);
        OffsetDateTime tomorrowPeriodTo = periodFrom.plusHours(1);
        HealthSyncEntry tomorrowEntity3 = createHealthSyncEntry(3L, syncId, sourceId, tomorrowPeriodFrom, tomorrowPeriodTo, 150.0, 75.0, 4.0);

        // 전체 엔트리 조회 (루프 종료)
        when(healthSyncEntryRepository.findHealthSyncEntryListBySyncIdOfPage(syncId, sourceId, 0, 100))
                .thenReturn(Arrays.asList(todayEntry1, todayEntity2, tomorrowEntity3));

        // when
        List<DailyHealthSyncEntryAggregate> result = sut.aggregateDailyHealthSyncDataByRecordKey(recordKey);

        // then
        assertEquals(2, result.size()); // 25일, 26일

        /*
        2025-03-25
        - activity = 100 + 200
        - calories = 50 + 100
        - distance = 2.0 + 3.0
         */
        DailyHealthSyncEntryAggregate dailyAggregateToday = result.stream()
                .filter(agg -> agg.date().equals(today))
                .findFirst()
                .orElse(null);
        assertNotNull(dailyAggregateToday);
        assertEquals(recordKey, dailyAggregateToday.recordKey());
        assertEquals(300.0, dailyAggregateToday.activityValue());
        assertEquals(150.0, dailyAggregateToday.calorieValue());
        assertEquals(5.0, dailyAggregateToday.distanceValue());

        /*
        2025-03-26
        - activity = 150
        - calories = 75
        - distance = 4.0
         */
        DailyHealthSyncEntryAggregate dailyAggregateTomorrow = result.stream()
                .filter(agg -> agg.date().equals(tomorrow))
                .findFirst()
                .orElse(null);
        assertNotNull(dailyAggregateTomorrow);
        assertEquals(recordKey, dailyAggregateTomorrow.recordKey());
        assertEquals(150.0, dailyAggregateTomorrow.activityValue());
        assertEquals(75.0, dailyAggregateTomorrow.calorieValue());
        assertEquals(4.0, dailyAggregateTomorrow.distanceValue());

        verify(healthSyncEntryRepository, times(1))
                .findHealthSyncEntryListBySyncIdOfPage(syncId, sourceId, 0, 100);
    }

    @Test
    @DisplayName("`aggregateAllDailyHealthSyncData` - 전체 레코드 대상 일별 건강 동기화 데이터 집계 테스트, 전체 레코드키를 기준으로 일일 단위로 집계가 되어야 한다.")
    public void testAggregateAllDailyHealthSyncData() {
        // given
        Map<Long, String> allSyncMap = new HashMap<>();
        allSyncMap.put(1L, "record-1");
        allSyncMap.put(2L, "record-2");
        when(healthSyncQueryService.findAllSyncIdRecordKeyMap()).thenReturn(allSyncMap);

        LocalDate today = LocalDate.of(2025, 3, 25);
        OffsetDateTime periodFrom = OffsetDateTime.of(today.atStartOfDay(), ZoneOffset.UTC);
        OffsetDateTime periodTo = periodFrom.plusHours(1);

        HealthSyncEntry entry1 = createHealthSyncEntry(1L, 1L, 10L, periodFrom, periodTo, 100.0, 50.0, 2.0);
        HealthSyncEntry entry2 = createHealthSyncEntry(2L, 2L, 20L, periodFrom, periodTo, 200.0, 100.0, 3.0);

        LocalDate tomorrow = LocalDate.of(2025, 3, 26);
        OffsetDateTime tomorrowPeriodFrom = OffsetDateTime.of(tomorrow.atStartOfDay(), ZoneOffset.UTC);
        OffsetDateTime tomorrowPeriodTo = tomorrowPeriodFrom.plusHours(1);

        HealthSyncEntry entry3 = createHealthSyncEntry(3L, 1L, 10L, tomorrowPeriodFrom, tomorrowPeriodTo, 100.0, 50.0, 2.0);
        HealthSyncEntry entry4 = createHealthSyncEntry(4L, 2L, 20L, tomorrowPeriodFrom, tomorrowPeriodTo, 200.0, 100.0, 3.0);

        when(healthSyncEntryRepository.findAllHealthSyncEntryListOfPage(0, 100))
                .thenReturn(Arrays.asList(entry1, entry2, entry3, entry4));

        // when
        List<DailyHealthSyncEntryAggregate> result = sut.aggregateAllDailyHealthSyncData();

        // then
        assertNotNull(result);
        assertEquals(4, result.size()); // 모든 레코드키에 대해 일일 단위로 집계, 레코드키 2개, 각 레코드 당 날짜 2개

        DailyHealthSyncEntryAggregate dailyAggregate1 = result.stream()
                .filter(agg -> agg.recordKey().equals("record-1"))
                .findFirst()
                .orElse(null);
        assertNotNull(dailyAggregate1);
        assertEquals(26, dailyAggregate1.date().getDayOfMonth()); // 날짜 내림 차순
        assertEquals(100.0, dailyAggregate1.activityValue());
        assertEquals(50.0, dailyAggregate1.calorieValue());
        assertEquals(2.0, dailyAggregate1.distanceValue());

        DailyHealthSyncEntryAggregate dailyAggregate2 = result.stream()
                .filter(agg -> agg.recordKey().equals("record-2"))
                .findFirst()
                .orElse(null);
        assertNotNull(dailyAggregate2);
        assertEquals(26, dailyAggregate2.date().getDayOfMonth()); // 날짜 내림 차순
        assertEquals(200.0, dailyAggregate2.activityValue());
        assertEquals(100.0, dailyAggregate2.calorieValue());
        assertEquals(3.0, dailyAggregate2.distanceValue());

        verify(healthSyncEntryRepository, times(1))
                .findAllHealthSyncEntryListOfPage(0, 100);
    }

    @Test
    @DisplayName("`aggregateAllMonthlyHealthSyncData` - 전체 레코드 대상 월별 건강 동기화 데이터 집계 테스트, 전체 레코드키를 기준으로 월간 단위로 집계가 되어야 한다.")
    public void testAggregateAllMonthlyHealthSyncData() {
        // given
        Map<Long, String> allSyncMap = new HashMap<>();
        allSyncMap.put(1L, "record-1");
        when(healthSyncQueryService.findAllSyncIdRecordKeyMap()).thenReturn(allSyncMap);

        // 같은 레코드 키로 서로 다른 월 엔트리 2개: 2025-03, 2025-04
        /*
          현재달, 레코드 키
         */
        LocalDate nowMonth = LocalDate.of(2025, 3, 1);
        OffsetDateTime periodFrom = OffsetDateTime.of(nowMonth.atStartOfDay(), ZoneOffset.UTC);
        OffsetDateTime periodTo = periodFrom.plusHours(1);

        HealthSyncEntry nowMonthEntry1 = createHealthSyncEntry(1L, 1L, 10L, periodFrom, periodTo, 100.0, 50.0, 2.0);
        HealthSyncEntry nowMonthEntry2 = createHealthSyncEntry(1L, 1L, 10L, periodFrom, periodTo, 100.0, 50.0, 2.0);

        /*
          다음달, 같은 레코드 키
         */
        LocalDate nextMonth = LocalDate.of(2025, 4, 1);
        OffsetDateTime nextMonthPeriodFrom = OffsetDateTime.of(nextMonth.atStartOfDay(), ZoneOffset.UTC);
        OffsetDateTime nextMonthPeriodTo = nextMonthPeriodFrom.plusHours(1);

        HealthSyncEntry nextMonthEntry1 = createHealthSyncEntry(2L, 1L, 10L, nextMonthPeriodFrom, nextMonthPeriodTo, 200.0, 100.0, 3.0);
        HealthSyncEntry nextMonthEntry2 = createHealthSyncEntry(2L, 1L, 10L, nextMonthPeriodFrom, nextMonthPeriodTo, 200.0, 100.0, 3.0);

        when(healthSyncEntryRepository.findAllHealthSyncEntryListOfPage(0, 100))
                .thenReturn(Arrays.asList(nowMonthEntry1, nowMonthEntry2, nextMonthEntry1, nextMonthEntry2));

        // when
        List<MonthlyHealthSyncEntryAggregate> result = sut.aggregateAllMonthlyHealthSyncData();

        // then
        assertNotNull(result);
        assertEquals(2, result.size()); // 2025-03: 1개, 2025-04: 1개

        MonthlyHealthSyncEntryAggregate nowMonthAggregate = result.stream()
                .filter(agg -> agg.recordKey().equals("record-1"))
                .filter(agg -> agg.date().equals("2025-03"))
                .findFirst()
                .orElse(null);
        assertNotNull(nowMonthAggregate);
        assertEquals(200.0, nowMonthAggregate.activityValue());
        assertEquals(100.0, nowMonthAggregate.calorieValue());
        assertEquals(4.0, nowMonthAggregate.distanceValue());

        MonthlyHealthSyncEntryAggregate nextMonthAggregate = result.stream()
                .filter(agg -> agg.recordKey().equals("record-1"))
                .filter(agg -> agg.date().equals("2025-04"))
                .findFirst()
                .orElse(null);
        assertNotNull(nextMonthAggregate);
        assertEquals(400.0, nextMonthAggregate.activityValue());
        assertEquals(200.0, nextMonthAggregate.calorieValue());
        assertEquals(6.0, nextMonthAggregate.distanceValue());

        verify(healthSyncEntryRepository, times(1))
                .findAllHealthSyncEntryListOfPage(0, 100);
    }
}