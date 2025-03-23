package com.kbhealthcare.ocare.healthSync.repository;

import com.kbhealthcare.ocare.healthSync.entity.HealthSyncEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthSyncEntryJpaRepository extends JpaRepository<HealthSyncEntry, Long> {
    
}
