package com.kbhealthcare.ocare.healthSync.service;

import com.kbhealthcare.ocare.healthSync.dto.HealthSyncEntryDto;
import com.kbhealthcare.ocare.healthSync.entity.HealthSync;
import com.kbhealthcare.ocare.healthSync.entity.HealthSyncEntry;
import com.kbhealthcare.ocare.healthSync.repository.HealthSyncEntryJpaRepository;
import com.kbhealthcare.ocare.healthSync.repository.HealthSyncJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthSyncEntryCommandService {
    private final HealthSyncJpaRepository healthSyncJpaRepository;
    private final HealthSyncEntryJpaRepository healthSyncEntryJpaRepository;

    @Transactional
    public void createHealthSyncEntry(Long syncId, Long sourceId, List<HealthSyncEntryDto> entryList) {
        HealthSync healthSync = healthSyncJpaRepository
                .findById(syncId).orElseThrow(() -> new RuntimeException("해당 syncId에 대한 건강 데이터가 없습니다."));

        List<HealthSyncEntry> healthSyncEntryList =
                entryList
                        .stream()
                        .map(entry -> entry.toEntity(syncId, sourceId, healthSync.getHealthSyncType()))
                        .toList();

        healthSyncEntryJpaRepository.saveAll(healthSyncEntryList);
    }
}
