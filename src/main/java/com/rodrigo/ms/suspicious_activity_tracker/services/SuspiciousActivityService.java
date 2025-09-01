package com.rodrigo.ms.suspicious_activity_tracker.services;

import org.springframework.stereotype.Service;

import com.rodrigo.ms.suspicious_activity_tracker.dto.RequestSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.dto.ResponseSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.entities.SuspiciousActivity;
import com.rodrigo.ms.suspicious_activity_tracker.mapper.SuspiciousActivityMapper;
import com.rodrigo.ms.suspicious_activity_tracker.repositories.SuspiciousActivityRepository;

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

        producerService.sendMessage(savedData);
        
        return dataMapper.toResponseDTO(savedData);
    }
}
