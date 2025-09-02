package com.rodrigo.ms.suspicious_activity_tracker.integration.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

import com.rodrigo.ms.suspicious_activity_tracker.dto.RequestSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.dto.ResponseSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.entities.SuspiciousActivity;
import com.rodrigo.ms.suspicious_activity_tracker.integration.config.TestContainerPostgresqlConfig;
import com.rodrigo.ms.suspicious_activity_tracker.mapper.SuspiciousActivityMapper;
import com.rodrigo.ms.suspicious_activity_tracker.repositories.SuspiciousActivityRepository;
import com.rodrigo.ms.suspicious_activity_tracker.services.SuspiciousActivityService;



public class SuspiciousActivityServiceTest extends TestContainerPostgresqlConfig{

    @Autowired 
    private SuspiciousActivityService service;

    @Autowired
    private SuspiciousActivityMapper mapper;

    @Autowired
    private SuspiciousActivityRepository repository;
    

    private SuspiciousActivity entity;
    private RequestSuspiciousActivityDTO request;
    private ResponseSuspiciousActivityDTO response;

    @BeforeEach
    public void setup(){
        entity = new SuspiciousActivity(UUID.randomUUID(), UUID.randomUUID(), "http://localhost:8080/resources", "192.168.1.2", "description", Instant.now(), Instant.now());
        
        request = mapper.toDTO(entity);
        response = mapper.toResponseDTO(entity);

    }



    @Test
    @DisplayName("Should create new suspicious activity")
    public void testCreateSuspiciousActivity(){
        
        var savedSuspiciousActivity = service.save(request);


        assertThat(savedSuspiciousActivity).isNotNull();
        assertThat(savedSuspiciousActivity.userId()).isEqualTo(response.userId());
        assertThat(savedSuspiciousActivity.ipAddress()).isEqualTo(response.ipAddress());
        assertThat(savedSuspiciousActivity.endpoint()).isEqualTo(response.endpoint());
        assertThat(savedSuspiciousActivity.description()).isEqualTo(response.description());
    }
    
}
