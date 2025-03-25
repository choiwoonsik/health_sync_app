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

    @PostMapping("health/sync")
    public ResponseEntity<Boolean> healthSync(
            @RequestBody HealthSyncInput healthSyncInput
    ) {
        boolean result = healthSyncCommandService.syncHealth(healthSyncInput.toDto());

        return ResponseEntity.ok(result);
    }
}
