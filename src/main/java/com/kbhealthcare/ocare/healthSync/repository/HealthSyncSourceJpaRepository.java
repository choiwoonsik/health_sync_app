package com.kbhealthcare.ocare.healthSync.repository;

import com.kbhealthcare.ocare.healthSync.entity.HealthSyncSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HealthSyncSourceJpaRepository extends JpaRepository<HealthSyncSource, Long> {
    Optional<HealthSyncSource> findBySyncId(Long syncId);
}
