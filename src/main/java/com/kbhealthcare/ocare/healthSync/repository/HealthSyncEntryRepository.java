package com.kbhealthcare.ocare.healthSync.repository;

import com.kbhealthcare.ocare.healthSync.entity.HealthSyncEntry;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kbhealthcare.ocare.healthSync.entity.QHealthSyncEntry.healthSyncEntry;

@Repository
public class HealthSyncEntryRepository extends QuerydslRepositorySupport {
    public HealthSyncEntryRepository() {
        super(HealthSyncEntryRepository.class);
    }

    @Transactional(readOnly = true)
    public List<HealthSyncEntry> findHealthSyncEntryListBySyncIdOfPage(Long syncId, Long sourceId, int offset, int pageSize) {
        return from(healthSyncEntry)
                .where(healthSyncEntry.syncId.eq(syncId))
                .where(healthSyncEntry.sourceId.eq(sourceId))
                .where(healthSyncEntry.id.gt(offset))
                .limit(pageSize)
                .orderBy(healthSyncEntry.id.asc())
                .fetch();
    }

    @Transactional(readOnly = true)
    public List<HealthSyncEntry> findAllHealthSyncEntryListOfPage(int offset, int pageSize) {
        return from(healthSyncEntry)
                .where(healthSyncEntry.id.gt(offset))
                .limit(pageSize)
                .orderBy(healthSyncEntry.id.asc())
                .fetch();
    }
}
