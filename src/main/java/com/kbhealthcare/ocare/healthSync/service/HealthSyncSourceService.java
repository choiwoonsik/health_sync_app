package com.kbhealthcare.ocare.healthSync.service;

import com.kbhealthcare.ocare.healthSync.repository.HealthSyncSourceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthSyncSourceService {
    private final HealthSyncSourceJpaRepository healthSyncSourceJpaRepository;
}
