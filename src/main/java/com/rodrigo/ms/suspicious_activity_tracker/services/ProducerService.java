package com.rodrigo.ms.suspicious_activity_tracker.services;

import java.time.Instant;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.rodrigo.ms.suspicious_activity_tracker.dto.MessageEventDTO;
import com.rodrigo.ms.suspicious_activity_tracker.dto.ResponseSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.enums.EventType;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProducerService {
    

    @Value("${kafka.topic.event-tickets}")
    private String topic;

    private final KafkaTemplate<String, MessageEventDTO> kafkaTemplate;


    public void sendMessage(ResponseSuspiciousActivityDTO payload, EventType eventType) {
        kafkaTemplate.send(topic, new MessageEventDTO(payload.id(), Instant.now(), eventType, payload));
    }
}
