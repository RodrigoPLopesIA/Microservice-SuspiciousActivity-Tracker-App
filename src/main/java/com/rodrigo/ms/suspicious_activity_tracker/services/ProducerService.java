package com.rodrigo.ms.suspicious_activity_tracker.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProducerService {
    

    @Value("${kafka.topic.event-tickets}")
    private String topic;

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void sendMessage(Object payload) {
        kafkaTemplate.send(topic, payload);
    }
}
