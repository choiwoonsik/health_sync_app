package com.kbhealthcare.ocare.healthSync.repository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.kbhealthcare.ocare.healthSync.entity.QHealthSync.healthSync;

@Repository
public class HealthSyncRepository extends QuerydslRepositorySupport {
    public HealthSyncRepository() {
        super(HealthSyncRepository.class);
    }

    public Long findHealthSyncIdByRecordId(String recordKey) {
        return from(healthSync)
                .select(healthSync.id)
                .where(healthSync.recordKey.eq(recordKey))
                .fetchOne();
    }
}
