package com.rodrigo.ms.suspicious_activity_tracker.services;

import org.springframework.stereotype.Service;

import com.rodrigo.ms.suspicious_activity_tracker.dto.RequestSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.repositories.SuspiciousActivityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SuspiciousActivityService {

    private final SuspiciousActivityRepository suspiciousActivityRepository;

    public void save(RequestSuspiciousActivityDTO data) {
        suspiciousActivityRepository.save(data);
    }
}
