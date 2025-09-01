package com.rodrigo.ms.suspicious_activity_tracker.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rodrigo.ms.suspicious_activity_tracker.dto.RequestSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.dto.ResponseSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.entities.SuspiciousActivity;
import com.rodrigo.ms.suspicious_activity_tracker.enums.EventType;
import com.rodrigo.ms.suspicious_activity_tracker.mapper.SuspiciousActivityMapper;
import com.rodrigo.ms.suspicious_activity_tracker.repositories.SuspiciousActivityRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SuspiciousActivityService {

    private final SuspiciousActivityRepository suspiciousActivityRepository;


    private final ProducerService producerService;

    private final SuspiciousActivityMapper dataMapper;

    public ResponseSuspiciousActivityDTO save(RequestSuspiciousActivityDTO data) {

        var dataToSave = dataMapper.toEntity(data);

        var savedData = suspiciousActivityRepository.save(dataToSave);
        var response = dataMapper.toResponseDTO(savedData);
        
        producerService.sendMessage(response, EventType.CREATED);

        return response;
    }

    public Page<ResponseSuspiciousActivityDTO> findAll(Pageable pageable) {
        var page = suspiciousActivityRepository.findAll(pageable);
        return page.map(dataMapper::toResponseDTO);
    }

    public ResponseSuspiciousActivityDTO findById(UUID id) {
        var entity = suspiciousActivityRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SuspiciousActivity not found with id: " + id));
        return dataMapper.toResponseDTO(entity);
    }

    public ResponseSuspiciousActivityDTO update(UUID id, RequestSuspiciousActivityDTO data) {
        var existing = suspiciousActivityRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SuspiciousActivity not found with id: " + id));
        
            dataMapper.updateEntityFromDto(data, existing);
        
        var updated = suspiciousActivityRepository.save(existing);
        
        var response = dataMapper.toResponseDTO(updated);
        
        producerService.sendMessage(response, EventType.UPDATED);
        return response;
    }

    public void delete(UUID id) {
        var entity = suspiciousActivityRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SuspiciousActivity not found with id: " + id));
        suspiciousActivityRepository.delete(entity);
        var response = dataMapper.toResponseDTO(entity);
        producerService.sendMessage(response, EventType.DELETED);
    }
}
