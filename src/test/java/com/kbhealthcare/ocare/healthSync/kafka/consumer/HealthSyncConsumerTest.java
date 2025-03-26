package com.kbhealthcare.ocare.healthSync.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbhealthcare.ocare.healthSync.dto.HealthSyncEntryChunkMessageDto;
import com.kbhealthcare.ocare.healthSync.dto.HealthSyncEntryDto;
import com.kbhealthcare.ocare.healthSync.service.HealthSyncEntryCommandService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HealthSyncConsumerTest {

    @Mock
    private HealthSyncEntryCommandService healthSyncEntryCommandService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private HealthSyncConsumer healthSyncConsumer;

    @Mock
    private Acknowledgment acknowledgment;

    @Test
    @DisplayName("consume() - JSON deserialization 성공 시 entries 생성, 이벤트 발행, ack 수행")
    public void testConsume_success() throws Exception {
        // given
        String topic = "health_sync";
        String key = "sampleKey";
        String jsonValue = "{\"dummy\":\"value\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>(topic, 0, 0L, key, jsonValue);
        List<ConsumerRecord<String, String>> records = Collections.singletonList(record);

        // HealthSyncEntryChunkMessageDto 모킹: syncId, sourceId, entries() 메서드
        HealthSyncEntryChunkMessageDto messageDto = mock(HealthSyncEntryChunkMessageDto.class);
        when(messageDto.syncId()).thenReturn(1L);
        when(messageDto.sourceId()).thenReturn(2L);
        List<HealthSyncEntryDto> entries = Collections.emptyList();
        when(messageDto.entries()).thenReturn(entries);

        when(objectMapper.readValue(jsonValue, HealthSyncEntryChunkMessageDto.class))
                .thenReturn(messageDto);

        // when
        healthSyncConsumer.consume(records, acknowledgment);

        // then
        // createHealthSyncEntry 호출 검증
        verify(healthSyncEntryCommandService, times(1))
                .createHealthSyncEntry(eq(1L), eq(2L), eq(entries));
        // acknowledge() 호출 검증
        verify(acknowledgment, times(1)).acknowledge();
    }

    @Test
    @DisplayName("consume() - JSON deserialization 실패 시 entries 미 생성, 이벤트 미 발행, ack 미 수행")
    public void testConsume_deserializationFailure() throws Exception {
        // given
        String topic = "health_sync";
        String key = "sampleKey";
        String jsonValue = "invalid-json";
        ConsumerRecord<String, String> record = new ConsumerRecord<>(topic, 0, 0L, key, jsonValue);
        List<ConsumerRecord<String, String>> records = Collections.singletonList(record);

        when(objectMapper.readValue(jsonValue, HealthSyncEntryChunkMessageDto.class))
                .thenThrow(new JsonProcessingException("error") {
                });

        // when & then : 예외 발생 확인
        Assertions.assertThrows(RuntimeException.class, () -> {
            healthSyncConsumer.consume(records, acknowledgment);
        });

        // acknowledge()는 정상적으로 호출되지 않아야 함
        verify(acknowledgment, never()).acknowledge();
        verify(healthSyncEntryCommandService, never()).createHealthSyncEntry(anyLong(), anyLong(), any());
    }
}