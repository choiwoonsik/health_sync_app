package com.kbhealthcare.ocare.healthSync.service;

import com.kbhealthcare.ocare.healthSync.repository.HealthSyncEntryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthSyncService {
    private final HealthSyncEntryJpaRepository healthSyncEntryJpaRepository;
}
