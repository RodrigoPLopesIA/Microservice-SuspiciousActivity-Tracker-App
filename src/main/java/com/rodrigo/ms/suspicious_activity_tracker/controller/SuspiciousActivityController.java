package com.rodrigo.ms.suspicious_activity_tracker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigo.ms.suspicious_activity_tracker.dto.RequestSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.dto.ResponseSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.services.ProducerService;
import com.rodrigo.ms.suspicious_activity_tracker.services.SuspiciousActivityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequiredArgsConstructor
@RestController
@RequestMapping("/suspicious_activiy")
public class SuspiciousActivityController {
    

    private final SuspiciousActivityService suspiciousActivityService;

    @PostMapping
    public ResponseEntity<ResponseSuspiciousActivityDTO> save(@Valid @RequestBody RequestSuspiciousActivityDTO request) {
        var saved = suspiciousActivityService.save(request);

        var uri = URI.create(String.format("/suspicious-activity/%s", saved.id()));
        
        return ResponseEntity.created(uri).body(saved);
    }
    
}
