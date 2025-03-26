package com.kbhealthcare.ocare.healthSync.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultKafkaProducer {
    private final KafkaTemplate<String, String> defaultKafkaProducerTemplate;
    private final ObjectMapper jacksonObjectMapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public <T> void publishKafka(String topic, String key, T data) {
        try {
            String jsonData = jacksonObjectMapper.writeValueAsString(data);
            defaultKafkaProducerTemplate
                    .send(topic, key, jsonData)
                    .whenComplete(
                            (record, e) -> {
                                if (e != null) {
                                    logger.warn("(topic: {}) publish failure. key: {}, data: {}", topic, key, data, e);
                                    throw new RuntimeException(e);
                                }
                            }
                    )
            ;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
