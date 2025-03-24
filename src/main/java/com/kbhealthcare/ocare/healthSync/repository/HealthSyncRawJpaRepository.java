package com.kbhealthcare.ocare.healthSync.repository;

import com.kbhealthcare.ocare.healthSync.entity.HealthSyncEntryRaw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthSyncRawJpaRepository extends JpaRepository<HealthSyncEntryRaw, Long> {
}
