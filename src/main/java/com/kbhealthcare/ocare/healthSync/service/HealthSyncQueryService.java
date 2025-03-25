package com.kbhealthcare.ocare.healthSync.service;

import com.kbhealthcare.ocare.healthSync.entity.HealthSync;
import com.kbhealthcare.ocare.healthSync.repository.HealthSyncJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HealthSyncQueryService {
    private final HealthSyncJpaRepository healthSyncJpaRepository;

    public Long findSyncIdByRecordKey(String recordKey) {
        return healthSyncJpaRepository.findByRecordKey(recordKey)
                .orElseThrow(() -> new RuntimeException("해당 레코드키에 대한 동기화 데이터가 없습니다."))
                .getId();
    }

    public Map<Long, String> findAllSyncIdRecordKeyMap() {
        return healthSyncJpaRepository.findAll()
                .stream()
                .collect(Collectors.toMap(HealthSync::getId, HealthSync::getRecordKey));
    }
}
