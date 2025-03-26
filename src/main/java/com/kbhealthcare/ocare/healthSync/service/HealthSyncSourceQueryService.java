package com.kbhealthcare.ocare.healthSync.service;

import com.kbhealthcare.ocare.healthSync.repository.HealthSyncSourceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HealthSyncSourceQueryService {
    private final HealthSyncSourceJpaRepository healthSyncSourceJpaRepository;

    @Transactional(readOnly = true)
    public Long findSourceIdBySyncId(Long syncId) {
        return healthSyncSourceJpaRepository.findBySyncId(syncId)
                .orElseThrow(() -> new RuntimeException("해당 syncId에 대한 sourceId가 없습니다."))
                .getId();
    }
}
