package com.kbhealthcare.ocare.healthSync.repository;

import com.kbhealthcare.ocare.healthSync.entity.HealthSync;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HealthSyncJpaRepository extends JpaRepository<HealthSync, Long> {
    Optional<HealthSync> findByRecordKey(String recordKey);

    Optional<Long> findIdByRecordKey(String recordKey);
}
