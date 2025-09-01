package com.rodrigo.ms.suspicious_activity_tracker.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestSuspiciousActivityDTO(
        @NotNull UUID userId,
        @NotBlank @Pattern(
            regexp = "^(https?:\\/\\/)?([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,}(:\\d+)?(\\/.*)?$", 
            message = "Invalid Endpoint"
            ) String endpoint,
        @NotBlank @Size(max = 45) @Pattern(regexp = "^[0-9.]+$", message = "Invalid IP Address") String ipAddress,
        @NotBlank String description) {

}
