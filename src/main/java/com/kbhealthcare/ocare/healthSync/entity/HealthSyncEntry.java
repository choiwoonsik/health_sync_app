package com.kbhealthcare.ocare.healthSync.entity;

import com.kbhealthcare.ocare.healthSync.common.BaseTimeEntity;
import com.kbhealthcare.ocare.healthSync.dto.ActivityType;
import com.kbhealthcare.ocare.healthSync.dto.Unit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Table(name = "health_sync_entries")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HealthSyncEntry extends BaseTimeEntity {
    @Column(nullable = false)
    private Long syncId;

    @Column(nullable = false)
    private Long sourceId;

    @Column(nullable = false)
    private OffsetDateTime periodFrom;

    @Column(nullable = false)
    private OffsetDateTime periodTo;

    @Column(nullable = false)
    private Double distanceValue;

    @Column(nullable = false)
    private Unit distanceUnit;

    @Column(nullable = false)
    private Double caloriesValue;

    @Column(nullable = false)
    private Unit caloriesUnit;

    @Column(nullable = false)
    private Double activityValue;

    @Column(nullable = false)
    private ActivityType activityType;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
