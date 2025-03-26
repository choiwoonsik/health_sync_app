package com.kbhealthcare.ocare.healthSync.controller;

import com.kbhealthcare.ocare.healthSync.input.HealthSyncInput;
import com.kbhealthcare.ocare.healthSync.service.HealthSyncCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class HealthSyncCommandController {
    private final HealthSyncCommandService healthSyncCommandService;

    /**
     * 건강 데이터 동기화
     * - INPUT 파일로 받은 Json 데이터를 DTO로 변환하여 동기화 처리
     * - record, source를 저장하고 entreis에 대한 raw 데이터를 저장한 후 Kafka 메시지를 발행한다.
     * @param healthSyncInput Josn 파일로 받은 데이터를 처리하는 DTO
     * @return 동기화 성공 여부
     */
    @PostMapping("health/sync")
    public ResponseEntity<Boolean> healthSync(
            @RequestBody HealthSyncInput healthSyncInput
    ) {
        boolean result = healthSyncCommandService.syncHealth(healthSyncInput.toDto());

        return ResponseEntity.ok(result);
    }
}
