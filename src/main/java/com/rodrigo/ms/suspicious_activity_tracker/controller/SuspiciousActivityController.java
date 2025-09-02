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
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequiredArgsConstructor
@RestController
@RequestMapping("/suspicious_activity")
public class SuspiciousActivityController {
    

    private final SuspiciousActivityService suspiciousActivityService;

    @PostMapping
    public ResponseEntity<ResponseSuspiciousActivityDTO> save(@Valid @RequestBody RequestSuspiciousActivityDTO request) {
        var saved = suspiciousActivityService.save(request);

        var uri = URI.create(String.format("/suspicious_activity/%s", saved.id()));
        
        return ResponseEntity.created(uri).body(saved);
    }

    @GetMapping
    public ResponseEntity<Page<ResponseSuspiciousActivityDTO>> findAll(
            Pageable pageable) {
        var page = suspiciousActivityService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseSuspiciousActivityDTO> findById(@PathVariable("id") UUID id) {
        var result = suspiciousActivityService.findById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseSuspiciousActivityDTO> update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody RequestSuspiciousActivityDTO request) {
        var updated = suspiciousActivityService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        suspiciousActivityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    
    
}
