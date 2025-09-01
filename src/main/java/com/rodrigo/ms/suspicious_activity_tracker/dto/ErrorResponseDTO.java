package com.rodrigo.ms.suspicious_activity_tracker.dto;

import java.util.Map;

public record ErrorResponseDTO(int status, String path, String message, Map<String, String> errors) {
    
}
