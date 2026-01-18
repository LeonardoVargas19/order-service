package org.leoromero.orderservice.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.leoromero.orderservice.model.dto.OrderPlacedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer; // El import correcto

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, OrderPlacedEvent> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // Usamos el constructor que acepta (Map, KeySerializer, ValueSerializer)
        return new DefaultKafkaProducerFactory<>(
                configProps,
                new StringSerializer(),
                new JsonSerializer<>()
        );
    }

    @Bean
    public KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}