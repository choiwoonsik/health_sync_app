package com.kbhealthcare.ocare.healthSync.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;

@Configuration
public class KafkaProducerConfig {
    @Value("${kafka.bootstrap.servers}")
    String healthSyncBrokers;

    @Bean
    public KafkaTemplate<String, String> defaultKafkaProducerTemplate() {
        HashMap<String, Object> producerConfig = new HashMap<>();
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, healthSyncBrokers);
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerConfig));
    }
}
