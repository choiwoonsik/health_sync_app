package com.kbhealthcare.ocare.healthSync.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbhealthcare.ocare.healthSync.dto.HealthSyncDto;
import com.kbhealthcare.ocare.healthSync.dto.HealthSyncEntryDto;
import com.kbhealthcare.ocare.healthSync.entity.HealthSyncEntryRaw;
import com.kbhealthcare.ocare.healthSync.repository.HealthSyncRawJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthSyncRawService {
    private final HealthSyncRawJpaRepository healthSyncRawJpaRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Long createHealthSyncRaw(Long syncId, Long sourceId, HealthSyncDto healthSyncDto) {
        // Kafka 메시지를 발행하기 전 DB에 저장한다.
        List<HealthSyncEntryDto> entryRawsData = healthSyncDto.data().entries();

        String healthSyncEntryRawsData;
        try {
            healthSyncEntryRawsData = objectMapper.writeValueAsString(entryRawsData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize HealthSyncEntryRaw data", e);
        }

        HealthSyncEntryRaw healthSyncEntryRaw = HealthSyncEntryRaw.builder()
                .syncId(syncId)
                .sourceId(sourceId)
                .rawData(healthSyncEntryRawsData)
                .build();

        return healthSyncRawJpaRepository.save(healthSyncEntryRaw).getId();
    }
}
