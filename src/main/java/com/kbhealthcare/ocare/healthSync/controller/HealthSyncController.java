package com.kbhealthcare.ocare.healthSync.controller;

import com.kbhealthcare.ocare.healthSync.input.HealthSyncInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/health-sync")
public class HealthSyncController {
    @PostMapping
    public ResponseEntity<Boolean> healthSync(
            @RequestBody HealthSyncInput healthSyncInput
    ) {
        /*
          todo: kafka producer
         */
        System.out.println(healthSyncInput);

        return ResponseEntity.ok(true);
    }
}
