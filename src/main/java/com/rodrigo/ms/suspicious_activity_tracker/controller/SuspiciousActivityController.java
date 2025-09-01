package com.rodrigo.ms.suspicious_activity_tracker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigo.ms.suspicious_activity_tracker.services.ProducerService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RequiredArgsConstructor
@RestController
@RequestMapping("/suspicious-activity")
public class SuspiciousActivityController {
    

    private final ProducerService producerService;

    @GetMapping
    public ResponseEntity<String> getMethodName() {
        this.producerService.sendMessage("mensagem do topico");
        return ResponseEntity.ok().body("mensagem enviada para o topico com sucesso!");
    }
    
}
