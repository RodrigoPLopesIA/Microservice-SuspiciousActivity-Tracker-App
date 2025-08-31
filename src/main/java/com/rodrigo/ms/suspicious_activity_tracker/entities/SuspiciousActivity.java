package com.rodrigo.ms.suspicious_activity_tracker.entities;

import java.util.UUID;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class SuspiciousActivity {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}
