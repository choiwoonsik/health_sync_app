package com.kbhealthcare.ocare.healthSync.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "health_sync_entry_raws")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HealthSyncEntryRaw {
    @Column(nullable = false)
    private Long syncId;

    @Column(nullable = false)
    private Long sourceId;

    @Column(nullable = false)
    private String rawData;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
}
