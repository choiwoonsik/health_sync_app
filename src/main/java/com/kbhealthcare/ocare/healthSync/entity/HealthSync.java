package com.kbhealthcare.ocare.healthSync.entity;

import com.kbhealthcare.ocare.healthSync.common.BaseTimeEntity;
import com.kbhealthcare.ocare.healthSync.dto.ActivityType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "health_syncs")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HealthSync extends BaseTimeEntity {
    @Column(nullable = false)
    private String recordKey;

    @Column(nullable = false)
    private ActivityType healthSyncType;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}