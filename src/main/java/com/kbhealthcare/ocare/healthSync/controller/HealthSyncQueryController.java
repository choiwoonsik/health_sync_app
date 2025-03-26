package com.kbhealthcare.ocare.healthSync.controller;

import com.kbhealthcare.ocare.healthSync.dto.aggregate.DailyHealthSyncEntryAggregate;
import com.kbhealthcare.ocare.healthSync.dto.aggregate.MonthlyHealthSyncEntryAggregate;
import com.kbhealthcare.ocare.healthSync.response.HealthSyncResponse;
import com.kbhealthcare.ocare.healthSync.service.HealthSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class HealthSyncQueryController {
    private final HealthSyncService healthSyncService;

    /**
     * 입력받은 레코드키에 대한 일별 건강동기화 데이터 목록을 집계하여 반환합니다.
     */
    @GetMapping("/health/daily")
    public ResponseEntity<List<HealthSyncResponse>> getDailyHealthSyncByRecordKey(
            @RequestParam(name = "recordKey") String recordKey
    ) {
        List<HealthSyncResponse> data = healthSyncService.getDailyHealthSyncByRecordKey(recordKey);

        return ResponseEntity.ok(data);
    }

    /**
     * 모든 레코드키에 대한 일별 건강동기화 데이터 목록을 집계하여 반환합니다.
     */
    @GetMapping("/health/daily/all")
    public ResponseEntity<List<HealthSyncResponse>> getAllDailyHealthSync() {
        List<HealthSyncResponse> data = healthSyncService.getAllDailyHealthSync();

        return ResponseEntity.ok(data);
    }

    /**
     * 모든 레코드키에 대한 월별 건강동기화 데이터 목록을 집계하여 반환합니다.
     */
    @GetMapping("/health/monthly/all")
    public ResponseEntity<List<HealthSyncResponse>> getAllMonthlyHealthSync() {
        List<HealthSyncResponse> data = healthSyncService.getAllMonthlyHealthSync();

        return ResponseEntity.ok(data);
    }
}
