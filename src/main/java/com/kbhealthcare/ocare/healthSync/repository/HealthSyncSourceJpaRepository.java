package com.kbhealthcare.ocare.healthSync.repository;

import com.kbhealthcare.ocare.healthSync.entity.HealthSyncSource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthSyncSourceJpaRepository extends JpaRepository<HealthSyncSource, Long> {
}
