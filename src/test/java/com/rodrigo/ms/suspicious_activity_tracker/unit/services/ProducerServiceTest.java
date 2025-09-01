package com.rodrigo.ms.suspicious_activity_tracker.unit.services;

import static org.mockito.Mockito.times;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import com.rodrigo.ms.suspicious_activity_tracker.dto.MessageEventDTO;
import com.rodrigo.ms.suspicious_activity_tracker.dto.ResponseSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.enums.EventType;
import com.rodrigo.ms.suspicious_activity_tracker.services.ProducerService;
import com.rodrigo.ms.suspicious_activity_tracker.services.SuspiciousActivityService;

@ExtendWith(MockitoExtension.class)
public class ProducerServiceTest {
    

    @InjectMocks
    private ProducerService service;

    @Mock
    private KafkaTemplate<String, MessageEventDTO> kafkaTemplate;

    MessageEventDTO message;
    ResponseSuspiciousActivityDTO payload;

    @Value("${kafka.topic.event-tickets}")
    String topic;

    @BeforeEach
    public void setup(){

        payload = new ResponseSuspiciousActivityDTO(UUID.randomUUID(), UUID.randomUUID(), "https://api.meuservico.com/v1/resource", "192.168.1.2",  "Usuário tentou acessar recurso não autorizado.", Instant.now(), Instant.now());
        message = new MessageEventDTO(payload.id(), Instant.now(), EventType.CREATED, payload);
    }

    @Test
    @DisplayName("should send message to topics")
    public void testSendMessage() {
        service.sendMessage(payload, message.eventType());

        Mockito.verify(kafkaTemplate, times(1)).send(
            Mockito.eq(topic),
            Mockito.any(MessageEventDTO.class)
        );

        Mockito.verifyNoMoreInteractions(kafkaTemplate);
    }
}
