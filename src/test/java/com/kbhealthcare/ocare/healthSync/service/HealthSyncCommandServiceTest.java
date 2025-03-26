package com.kbhealthcare.ocare.healthSync.service;

import com.kbhealthcare.ocare.healthSync.dto.ActivityType;
import com.kbhealthcare.ocare.healthSync.dto.HealthSyncDto;
import com.kbhealthcare.ocare.healthSync.dto.HealthSyncEntryDto;
import com.kbhealthcare.ocare.healthSync.dto.HealthSyncEntryGroupDto;
import com.kbhealthcare.ocare.healthSync.dto.HealthSyncSourceDto;
import com.kbhealthcare.ocare.healthSync.dto.event.HealthSyncEvent;
import com.kbhealthcare.ocare.healthSync.entity.HealthSync;
import com.kbhealthcare.ocare.healthSync.entity.HealthSyncSource;
import com.kbhealthcare.ocare.healthSync.repository.HealthSyncJpaRepository;
import com.kbhealthcare.ocare.healthSync.repository.HealthSyncSourceJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HealthSyncCommandServiceTest {

    @Mock
    private HealthSyncRawService healthSyncRawService;

    @Mock
    private HealthSyncJpaRepository healthSyncJpaRepository;

    @Mock
    private HealthSyncSourceJpaRepository healthSyncSourceJpaRepository;

    @Mock
    private org.springframework.context.ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private HealthSyncCommandService sut;

    @Mock
    private HealthSyncDto healthSyncDto;

    @Mock
    private HealthSyncEntryGroupDto healthSyncDataDto;

    @Mock
    private HealthSyncSourceDto healthSyncSourceDto;

    @Mock
    private List<HealthSyncEntryDto> entryDtos;

    @Test
    @DisplayName("`syncHealth` - 신규 동기화 데이터 저장 테스트, 새로 저장도 되고 이벤트도 발행되어야 함")
    public void testSyncHealth_NewRecord() {
        // given
        String recordKey = "record-1";
        when(healthSyncDto.recordKey()).thenReturn(recordKey);
        when(healthSyncDto.data()).thenReturn(healthSyncDataDto);
        when(healthSyncDataDto.source()).thenReturn(healthSyncSourceDto);
        when(healthSyncDataDto.entries()).thenReturn(entryDtos);

        // 신규 동기화 데이터인 경우, HealthSyncJpaRepository.findByRecordKey()가 빈 Optional을 반환
        when(healthSyncJpaRepository.findByRecordKey(recordKey)).thenReturn(Optional.empty());

        // HealthSyncDto.toEntity()를 통해 생성된 엔티티
        HealthSync newHealthSync = HealthSync.builder()
                .recordKey(recordKey)
                .healthSyncType(ActivityType.STEPS)
                .build();
        when(healthSyncDto.toEntity()).thenReturn(newHealthSync);

        // 저장 시 id가 부여된 엔티티 반환
        HealthSync savedHealthSync = HealthSync.builder()
                .id(1L)
                .recordKey(recordKey)
                .healthSyncType(ActivityType.STEPS)
                .build();
        when(healthSyncJpaRepository.save(newHealthSync)).thenReturn(savedHealthSync);

        // 신규 HealthSyncSource인 경우, HealthSyncSourceJpaRepository.findBySyncId(1L) 빈 Optional 반환
        when(healthSyncSourceJpaRepository.findBySyncId(1L)).thenReturn(Optional.empty());

        // healthSyncSourceDto.toEntity(1L) 호출 시 신규 HealthSyncSource 생성
        HealthSyncSource newHealthSyncSource = HealthSyncSource.builder()
                .syncId(1L)
                .deviceName("신규 디바이스")
                .deviceVender("신규 벤더")
                .sourceMode(1)
                .sourceName("신규 출처")
                .sourceType("신규 타입")
                .build();
        when(healthSyncSourceDto.toEntity(1L)).thenReturn(newHealthSyncSource);

        // 저장 시 id가 부여된 HealthSyncSource 반환
        HealthSyncSource savedHealthSyncSource = HealthSyncSource.builder()
                .id(10L)
                .syncId(1L)
                .deviceName("신규 디바이스")
                .deviceVender("신규 벤더")
                .sourceMode(1)
                .sourceName("신규 출처")
                .sourceType("신규 타입")
                .build();
        when(healthSyncSourceJpaRepository.save(newHealthSyncSource)).thenReturn(savedHealthSyncSource);

        // 발행된 이벤트 확인
        ArgumentCaptor<HealthSyncEvent> eventCaptor = ArgumentCaptor.forClass(HealthSyncEvent.class);

        // when
        boolean result = sut.syncHealth(healthSyncDto);

        // then
        assertTrue(result);
        verify(healthSyncJpaRepository, times(1)).findByRecordKey(recordKey);
        verify(healthSyncJpaRepository, times(1)).save(newHealthSync);
        verify(healthSyncSourceJpaRepository, times(1)).findBySyncId(1L);
        verify(healthSyncSourceJpaRepository, times(1)).save(newHealthSyncSource);
        verify(healthSyncRawService, times(1)).createHealthSyncRaw(1L, 10L, healthSyncDto);

        // 발행된 이벤트 확인
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
        HealthSyncEvent publishedEvent = eventCaptor.getValue();
        assertEquals(1L, publishedEvent.syncId());
        assertEquals(10L, publishedEvent.sourceId());
        assertEquals(entryDtos, publishedEvent.entries());
    }

    @Test
    @DisplayName("`syncHealth` - 이미 있는 레코드/출처에 대한 추가 동기화 데이터 저장 테스트, 레코드, 출저 정보는 중복 저장 되지 않고 이벤트만 발행되어야 함")
    public void testSyncHealth_ExistingRecord() {
        // given
        String recordKey = "record-1";
        when(healthSyncDto.recordKey()).thenReturn(recordKey);
        when(healthSyncDto.data()).thenReturn(healthSyncDataDto);
        when(healthSyncDataDto.entries()).thenReturn(entryDtos);

        // 기존 HealthSync가 존재하는 경우
        HealthSync existingHealthSync = HealthSync.builder()
                .id(1L)
                .recordKey(recordKey)
                .healthSyncType(ActivityType.STEPS)
                .build();
        when(healthSyncJpaRepository.findByRecordKey(recordKey)).thenReturn(Optional.of(existingHealthSync));

        // 기존 HealthSyncSource가 존재하는 경우
        HealthSyncSource existingSource = HealthSyncSource.builder()
                .id(10L)
                .syncId(1L)
                .deviceName("기존 디바이스")
                .deviceVender("기존 벤더사")
                .sourceMode(1)
                .sourceName("기존 출처")
                .sourceType("기존 타입")
                .build();
        when(healthSyncSourceJpaRepository.findBySyncId(1L)).thenReturn(Optional.of(existingSource));

        // 이벤트 확인
        ArgumentCaptor<HealthSyncEvent> eventCaptor = ArgumentCaptor.forClass(HealthSyncEvent.class);

        // when
        boolean result = sut.syncHealth(healthSyncDto);

        // then
        assertTrue(result);
        verify(healthSyncJpaRepository, times(1)).findByRecordKey(recordKey);

        // 기존 데이터가 있으므로 save()는 호출되지 않아야 함
        verify(healthSyncJpaRepository, never()).save(any(HealthSync.class));
        verify(healthSyncSourceJpaRepository, times(1)).findBySyncId(1L);
        verify(healthSyncSourceJpaRepository, never()).save(any(HealthSyncSource.class));
        verify(healthSyncRawService, times(1)).createHealthSyncRaw(1L, 10L, healthSyncDto);

        // 발행된 이벤트 확인
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
        HealthSyncEvent publishedEvent = eventCaptor.getValue();
        assertEquals(1L, publishedEvent.syncId());
        assertEquals(10L, publishedEvent.sourceId());
        assertEquals(entryDtos, publishedEvent.entries());
    }
}
