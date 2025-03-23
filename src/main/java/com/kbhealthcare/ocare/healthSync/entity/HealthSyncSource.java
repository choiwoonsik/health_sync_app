package com.kbhealthcare.ocare.healthSync.entity;

import com.kbhealthcare.ocare.healthSync.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "health_sync_sources")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HealthSyncSource extends BaseTimeEntity {
    @Column(nullable = false)
    private Long syncId;

    @Column(nullable = false)
    private String deviceName;

    @Column(nullable = false)
    private String deviceVendor;

    @Column(nullable = false)
    private int sourceMode;

    @Column(nullable = false)
    private String sourceName;

    @Column(nullable = false)
    private String sourceType;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}

