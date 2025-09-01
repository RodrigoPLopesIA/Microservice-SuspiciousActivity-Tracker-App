package com.rodrigo.ms.suspicious_activity_tracker.unit.services;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rodrigo.ms.suspicious_activity_tracker.dto.RequestSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.dto.ResponseSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.entities.SuspiciousActivity;
import com.rodrigo.ms.suspicious_activity_tracker.enums.EventType;
import com.rodrigo.ms.suspicious_activity_tracker.mapper.SuspiciousActivityMapper;
import com.rodrigo.ms.suspicious_activity_tracker.repositories.SuspiciousActivityRepository;
import com.rodrigo.ms.suspicious_activity_tracker.services.ProducerService;
import com.rodrigo.ms.suspicious_activity_tracker.services.SuspiciousActivityService;

@ExtendWith(MockitoExtension.class)
public class SuspiciousActivityServiceTest {
    


    @InjectMocks
    public SuspiciousActivityService service;


    @Mock
    public SuspiciousActivityRepository repository;


    @Mock
    public SuspiciousActivityMapper mapper;


    @Mock
    private ProducerService producerService;

    RequestSuspiciousActivityDTO request;
    ResponseSuspiciousActivityDTO response;
    SuspiciousActivity suspiciousActivity;

    @BeforeEach
    public void setup(){
        suspiciousActivity = new SuspiciousActivity(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "http://localhost:8080/resource",
            "192.168.1.2",
            "Suspicious activity",
            Instant.now(),
            Instant.now()
        );

        request = new RequestSuspiciousActivityDTO(
            suspiciousActivity.getUserId(), 
            suspiciousActivity.getEndpoint(), 
            suspiciousActivity.getIpAddress(), 
            suspiciousActivity.getDescription()
        );
     

        response = new ResponseSuspiciousActivityDTO(
            suspiciousActivity.getId(),
            suspiciousActivity.getUserId(),
            suspiciousActivity.getEndpoint(),
            suspiciousActivity.getIpAddress(),
            suspiciousActivity.getDescription(),
            suspiciousActivity.getCreatedAt(),
            suspiciousActivity.getUpdatedAt()
        );
    }


    @Test
    @DisplayName("should create a new suspicious activity")
    public void testCreateSuspiciousActivity(){
        // Arrange
        Mockito.when(mapper.toEntity(Mockito.any(RequestSuspiciousActivityDTO.class)))
            .thenReturn(suspiciousActivity);
        Mockito.when(mapper.toResponseDTO(suspiciousActivity))
            .thenReturn(response);
        Mockito.when(repository.save(Mockito.any(SuspiciousActivity.class)))
            .thenReturn(suspiciousActivity);

        // Act
        var result = service.save(request);

        // Assert
        assertThat(result).isNotNull();
        Mockito.verify(producerService).sendMessage(Mockito.any(ResponseSuspiciousActivityDTO.class),
                                                    Mockito.eq(EventType.CREATED));
    }
}
