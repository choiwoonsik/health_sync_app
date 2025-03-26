package com.kbhealthcare.ocare.healthSync.listener;

import com.kbhealthcare.ocare.healthSync.common.StreamChunkDelegator;
import com.kbhealthcare.ocare.healthSync.config.KafkaTopics;
import com.kbhealthcare.ocare.healthSync.dto.HealthSyncEntryChunkMessageDto;
import com.kbhealthcare.ocare.healthSync.dto.HealthSyncEntryDto;
import com.kbhealthcare.ocare.healthSync.dto.event.HealthSyncEvent;
import com.kbhealthcare.ocare.healthSync.kafka.producer.DefaultKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class HealthSyncListener {
    private final DefaultKafkaProducer defaultKafkaProducer;
    private final ThreadPoolTaskExecutor eventAsyncExecutor;

    /**
     * 건강 데이터 동기화 이벤트를 받아서 Kafka 메시지로 발행한다.
     * - entries를 CHUNK_SIZE 만큼 나눠서 Kafka 메시지로 발행한다.
     * - sync_id, source_id, raw_Id를 이용해서 메시지 유실 시 재발행을 요청한다.
     * @param event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishHealthSync(HealthSyncEvent event) {
        Stream<HealthSyncEntryDto> entryDtoStream = event.entries().stream();
        List<List<HealthSyncEntryDto>> chunkedEntries = StreamChunkDelegator.chunkStream(entryDtoStream, CHUNK_SIZE);

        int totalChunks = chunkedEntries.size();

        for (int index = 0; index < totalChunks; index++) {
            List<HealthSyncEntryDto> healthSyncEntryDtos = chunkedEntries.get(index);

            HealthSyncEntryChunkMessageDto messageDto = new HealthSyncEntryChunkMessageDto(
                    event.syncId(),
                    event.sourceId(),
                    event.RawId(),
                    index,
                    totalChunks,
                    healthSyncEntryDtos
            );

            String key = event.syncId() + ":" + event.sourceId() + ":" + index;

            CompletableFuture.runAsync(
                    () -> defaultKafkaProducer.publishKafka(KafkaTopics.HEALTH_SYNC, key, messageDto),
                    eventAsyncExecutor
            );
        }
    }

    private static final int CHUNK_SIZE = 100;
}
