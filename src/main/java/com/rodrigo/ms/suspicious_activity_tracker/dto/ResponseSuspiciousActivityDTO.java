package com.rodrigo.ms.suspicious_activity_tracker.dto;

import java.time.Instant;
import java.util.UUID;


public record ResponseSuspiciousActivityDTO(
        UUID id,
        UUID userId,
        String endpoint,
        String ipAddress,
        String description,
        Instant createdAt,
        Instant updatedAt
        ) {

}
