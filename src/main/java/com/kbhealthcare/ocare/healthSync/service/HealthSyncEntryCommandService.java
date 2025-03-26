package com.kbhealthcare.ocare.healthSync.service;

import com.kbhealthcare.ocare.healthSync.dto.HealthSyncEntryDto;
import com.kbhealthcare.ocare.healthSync.entity.HealthSync;
import com.kbhealthcare.ocare.healthSync.entity.HealthSyncEntry;
import com.kbhealthcare.ocare.healthSync.repository.HealthSyncEntryJpaRepository;
import com.kbhealthcare.ocare.healthSync.repository.HealthSyncJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthSyncEntryCommandService {
    private final HealthSyncJpaRepository healthSyncJpaRepository;
    private final HealthSyncEntryJpaRepository healthSyncEntryJpaRepository;
    private final HealthSyncCacheService healthSyncCacheService;
    private final ApplicationEventPublisher applicationEventPublisher;

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

        applicationEventPublisher.publishEvent(healthSync.getRecordKey());
        log.info("HealthSyncEntry 저장 완료: syncId={}, sourceId={}, entrySize={}", syncId, sourceId, healthSyncEntryList.size());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void evictHealthSyncRecordKey(String recordKey) {
        log.info("Evict cache for recordKey: {}", recordKey);
        healthSyncCacheService.evictDailyCache(recordKey);
        healthSyncCacheService.evictMonthlyCache(recordKey);
    }
}
