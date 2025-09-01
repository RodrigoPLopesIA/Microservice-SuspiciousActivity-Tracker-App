package com.rodrigo.ms.suspicious_activity_tracker.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigo.ms.suspicious_activity_tracker.entities.SuspiciousActivity;

public interface SuspiciousActivityRepository extends JpaRepository<SuspiciousActivity, UUID> {
    
}
