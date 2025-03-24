package com.kbhealthcare.ocare.healthSync.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Value("${kafka.bootstrap.servers}")
    String healthSyncBrokers;

    @Value("${kafka.consumer.group.id}")
    private String consumerGroupId;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> healthSyncKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(this.consumerFactory(healthSyncBrokers, consumerGroupId));
        factory.setBatchListener(true);
        factory.getContainerProperties().setPollTimeout(60000L);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        return factory;
    }

    private ConsumerFactory<String, String> consumerFactory(String brokers, String consumerGroupId) {
        HashMap<String, Object> configProps = this.consumerProperties(brokers, consumerGroupId);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    private HashMap<String, Object> consumerProperties(String brokers, String consumerGroupId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        map.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        map.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        map.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return map;
    }
}