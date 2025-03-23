package com.kbhealthcare.ocare.healthSync.repository;

import com.kbhealthcare.ocare.healthSync.entity.HealthSync;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthSyncJpaRepository extends JpaRepository<HealthSync, Long> {

}
