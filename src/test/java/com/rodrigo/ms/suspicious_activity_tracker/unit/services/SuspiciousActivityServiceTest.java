package com.rodrigo.ms.suspicious_activity_tracker.unit.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

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

import jakarta.persistence.EntityNotFoundException;

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

    @Test
    @DisplayName("should update a suspicious activity")
    public void testUpdateSuspiciousActivity() {
        // Arrange
        var id = UUID.randomUUID();

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(suspiciousActivity));
        Mockito.when(mapper.toResponseDTO(suspiciousActivity))
            .thenReturn(response);
        Mockito.when(repository.save(Mockito.any(SuspiciousActivity.class)))
            .thenReturn(suspiciousActivity);

        // Act
        var result = service.update(id, request);

        // Assert
        assertThat(result).isNotNull();

        Mockito.verify(mapper).updateEntityFromDto(Mockito.any(RequestSuspiciousActivityDTO.class),
                                                Mockito.any(SuspiciousActivity.class));
        Mockito.verify(producerService).sendMessage(Mockito.any(ResponseSuspiciousActivityDTO.class),
                                                    Mockito.eq(EventType.UPDATED));
    }

    @Test
    @DisplayName("should throw a error when try to update suspicious activity not exists")
    public void testUpdateSuspiciousActivityEntityNotFound() {
        // Arrange
        var id = UUID.randomUUID();

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());


        // Assert & Act
        assertThatThrownBy(() -> service.update(id, request))
        .isInstanceOf(EntityNotFoundException.class).hasMessage("SuspiciousActivity not found with id: " + id);



        Mockito.verify(repository, never()).save(Mockito.any(SuspiciousActivity.class));
        Mockito.verify(mapper, never()).updateEntityFromDto(Mockito.any(RequestSuspiciousActivityDTO.class),
                                                Mockito.any(SuspiciousActivity.class));
        Mockito.verify(producerService, never()).sendMessage(Mockito.any(ResponseSuspiciousActivityDTO.class),
                                                    Mockito.eq(EventType.UPDATED));
    }

    @Test
    @DisplayName("should throw a error when try to delete suspicious activity not exists")
    public void testDeleteSuspiciousActivityEntityNotFound() {
        // Arrange
        var id = UUID.randomUUID();

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());


        // Assert & Act
        assertThatThrownBy(() -> service.delete(id))
        .isInstanceOf(EntityNotFoundException.class).hasMessage("SuspiciousActivity not found with id: " + id);



        Mockito.verify(repository, never()).delete(Mockito.any(SuspiciousActivity.class));
        Mockito.verify(mapper, never()).toResponseDTO(Mockito.any(SuspiciousActivity.class));
        Mockito.verify(producerService, never()).sendMessage(Mockito.any(ResponseSuspiciousActivityDTO.class),
                                                    Mockito.eq(EventType.DELETED));
    }

    @Test
    @DisplayName("should delete suspicious activity")
    public void testDeleteSuspiciousActivityEntity() {
        // Arrange
        var id = UUID.randomUUID();

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(suspiciousActivity));
        Mockito.doNothing().when(repository).delete(Mockito.any(SuspiciousActivity.class));

        Mockito.when(mapper.toResponseDTO(suspiciousActivity)).thenReturn(response);

        // Assert & Act
        service.delete(id);

        Mockito.verify(producerService, times(1)).sendMessage(Mockito.any(ResponseSuspiciousActivityDTO.class),
                                                    Mockito.eq(EventType.DELETED));
        Mockito.verify(repository, times(1)).delete(Mockito.any(SuspiciousActivity.class));
    }
}
