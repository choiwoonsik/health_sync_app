package com.kbhealthcare.ocare.healthSync.service;

import com.kbhealthcare.ocare.healthSync.dto.HealthSyncDto;
import com.kbhealthcare.ocare.healthSync.dto.event.HealthSyncEvent;
import com.kbhealthcare.ocare.healthSync.entity.HealthSync;
import com.kbhealthcare.ocare.healthSync.entity.HealthSyncSource;
import com.kbhealthcare.ocare.healthSync.repository.HealthSyncJpaRepository;
import com.kbhealthcare.ocare.healthSync.repository.HealthSyncSourceJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HealthSyncCommandService {
    private final HealthSyncRawService healthSyncRawService;
    private final HealthSyncJpaRepository healthSyncJpaRepository;
    private final HealthSyncSourceJpaRepository healthSyncSourceJpaRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public boolean syncHealth(HealthSyncDto healthSyncDto) {
        String recordKey = healthSyncDto.recordKey();

        /*
        1. 건강 데이터 Sync, Source 데이터를 우선 저장한다.
         */
        Long syncId;
        Optional<HealthSync> healthSync = healthSyncJpaRepository.findByRecordKey(recordKey);

        if (healthSync.isEmpty()) {
            HealthSync syncEntity = healthSyncDto.toEntity();
            syncId = healthSyncJpaRepository.save(syncEntity).getId();
        } else {
            syncId = healthSync.get().getId();
        }

        Long sourceId;
        Optional<HealthSyncSource> healthSyncSource = healthSyncSourceJpaRepository.findBySyncId(syncId);

        if (healthSyncSource.isEmpty()) {
            HealthSyncSource sourceEntity = healthSyncDto.data().source().toEntity(syncId);
            sourceId = healthSyncSourceJpaRepository.save(sourceEntity).getId();
        } else {
            sourceId = healthSyncSource.get().getId();
        }

        /*
        2. Kafka 메시지를 발행하기 전 entries 에 대한 raw 데이터를 저장한다.
        TODO: 메시지 유실 시 해당 DB 조회 후 재발행 처리 (조회는 syncId, sourceId 로 조회)
         */
        Long rawId = healthSyncRawService.createHealthSyncRaw(syncId, sourceId, healthSyncDto);

        // 3. 건강 데이터 동기화를 위해 Kafka 메시지를 발행한다.
        HealthSyncEvent event = new HealthSyncEvent(syncId, sourceId, rawId, healthSyncDto.data().entries());
        eventPublisher.publishEvent(event);

        return true;
    }
}
