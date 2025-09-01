package com.rodrigo.ms.suspicious_activity_tracker.integration;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import com.rodrigo.ms.suspicious_activity_tracker.dto.MessageEventDTO;
import com.rodrigo.ms.suspicious_activity_tracker.dto.ResponseSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.enums.EventType;
import com.rodrigo.ms.suspicious_activity_tracker.services.ProducerService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(topics = {
        "event_tickets" }, partitions = 1, bootstrapServersProperty = "spring.kafka.bootstrap-servers", brokerProperties = {
                "auto.create.topics.enable=true"
        })
public class ProducerServiceTest {
    
    @Autowired
    private ProducerService producerService;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Value("${kafka.topic.event-tickets}")
    String topic;

    MessageEventDTO message;
    ResponseSuspiciousActivityDTO payload;
    
    @BeforeEach
    public void setup(){
        payload = new ResponseSuspiciousActivityDTO(UUID.randomUUID(), UUID.randomUUID(), "https://api.meuservico.com/v1/resource", "192.168.1.2",  "Usuário tentou acessar recurso não autorizado.", Instant.now(), Instant.now());
        message = new MessageEventDTO(payload.id(), Instant.now(), EventType.CREATED, payload);
    }

    @Test
    @DisplayName("should produce and consume a message successfully")
    void testProducerSendsMessage() {
        var consumer = this.createConsumer();

        producerService.sendMessage(payload, EventType.CREATED);

        ConsumerRecords<String, MessageEventDTO> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(7));

        assertThat(records).hasSize(1);
        assertThat(records.iterator().next().value().payload()).isEqualTo(payload);
        assertThat(records.iterator().next().value().id()).isEqualTo(payload.id());

        consumer.close();
    }

    Consumer<String, MessageEventDTO> createConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("ticket-group", "true", embeddedKafkaBroker);

        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        Consumer<String, MessageEventDTO> consumer = new DefaultKafkaConsumerFactory<>(consumerProps,
                new StringDeserializer(), new JsonDeserializer<>(MessageEventDTO.class))
                .createConsumer();

        embeddedKafkaBroker.consumeFromEmbeddedTopics(consumer, topic);
        
        return consumer;
    }
}
