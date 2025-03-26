package com.kbhealthcare.ocare.healthSync.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbhealthcare.ocare.healthSync.config.KafkaTopics;
import com.kbhealthcare.ocare.healthSync.dto.HealthSyncEntryChunkMessageDto;
import com.kbhealthcare.ocare.healthSync.service.HealthSyncEntryCommandService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HealthSyncConsumer {
    private final HealthSyncEntryCommandService healthSyncEntryCommandService;
    private final ObjectMapper om;

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @KafkaListener(
            topics = KafkaTopics.HEALTH_SYNC,
            groupId = "${kafka.consumer.group.id}",
            containerFactory = "healthSyncKafkaListenerContainerFactory"
    )
    public void consume(
            List<ConsumerRecord<String, String>> records,
            Acknowledgment acknowledgement
    ) {
        for (ConsumerRecord<String, String> record : records) {
            String value = record.value();
            logger.info("Received Message - topic = {}, key = {}, value = {}", record.topic(), record.key(), value);

            HealthSyncEntryChunkMessageDto messageDto;

            try {
                messageDto = om.readValue(value, HealthSyncEntryChunkMessageDto.class);

                /*
                  TODO: chunkIndex/totalChunks와 messageDto.rawId를 이용하여 메시지 유실을 파악하고 재발행을 요청한다
                 */

                healthSyncEntryCommandService.createHealthSyncEntry(
                        messageDto.syncId(),
                        messageDto.sourceId(),
                        messageDto.entries()
                );
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        acknowledgement.acknowledge();
    }
}
