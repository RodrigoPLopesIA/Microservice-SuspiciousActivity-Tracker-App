package com.rodrigo.ms.suspicious_activity_tracker.dto;

import java.time.Instant;
import java.util.UUID;

import com.rodrigo.ms.suspicious_activity_tracker.enums.EventType;

public record MessageEventDTO(UUID id, Instant timestamp, EventType eventType, ResponseSuspiciousActivityDTO payload) {
    
}
