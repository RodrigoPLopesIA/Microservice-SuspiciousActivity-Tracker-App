package com.rodrigo.ms.suspicious_activity_tracker.integration.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rodrigo.ms.suspicious_activity_tracker.dto.RequestSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.dto.ResponseSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.entities.SuspiciousActivity;
import com.rodrigo.ms.suspicious_activity_tracker.integration.config.TestContainerPostgresqlConfig;
import com.rodrigo.ms.suspicious_activity_tracker.mapper.SuspiciousActivityMapper;
import com.rodrigo.ms.suspicious_activity_tracker.repositories.SuspiciousActivityRepository;
import com.rodrigo.ms.suspicious_activity_tracker.services.SuspiciousActivityService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
public class SuspiciousActivityServiceTest extends TestContainerPostgresqlConfig {

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
    public void setup() {
        repository.deleteAll(); 
        entity = new SuspiciousActivity(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "http://localhost:8080/resources",
                "192.168.1.2",
                "description",
                Instant.now(),
                Instant.now()
        );
        request = mapper.toDTO(entity);
        response = mapper.toResponseDTO(entity);
    }

    @Test
    @DisplayName("Should create new suspicious activity")
    public void testCreateSuspiciousActivity() {
        var savedSuspiciousActivity = service.save(request);

        assertThat(savedSuspiciousActivity).isNotNull();
        assertThat(savedSuspiciousActivity.userId()).isEqualTo(response.userId());
        assertThat(savedSuspiciousActivity.ipAddress()).isEqualTo(response.ipAddress());
        assertThat(savedSuspiciousActivity.endpoint()).isEqualTo(response.endpoint());
        assertThat(savedSuspiciousActivity.description()).isEqualTo(response.description());
    }

    @Test
    @DisplayName("Should find suspicious activity by id")
    public void testFindById() {
        var saved = service.save(request);
        var found = service.findById(saved.id());

        assertThat(found).isNotNull();
        assertThat(found.id()).isEqualTo(saved.id());
        assertThat(found.userId()).isEqualTo(saved.userId());
    }

    @Test
    @DisplayName("Should throw when finding non-existent suspicious activity by id")
    public void testFindByIdNotFound() {
        UUID randomId = UUID.randomUUID();
        assertThatThrownBy(() -> service.findById(randomId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("SuspiciousActivity not found");
    }

    @Test
    @DisplayName("Should update suspicious activity")
    public void testUpdateSuspiciousActivity() {
        var saved = service.save(request);

        RequestSuspiciousActivityDTO updateRequest = new RequestSuspiciousActivityDTO(
                saved.userId(),
                "http://localhost:8080/updated",
                "10.0.0.1",
                "updated description"
        );

        var updated = service.update(saved.id(), updateRequest);

        assertThat(updated).isNotNull();
        assertThat(updated.id()).isEqualTo(saved.id());
        assertThat(updated.endpoint()).isEqualTo("http://localhost:8080/updated");
        assertThat(updated.ipAddress()).isEqualTo("10.0.0.1");
        assertThat(updated.description()).isEqualTo("updated description");
    }

    @Test
    @DisplayName("Should throw when updating non-existent suspicious activity")
    public void testUpdateSuspiciousActivityNotFound() {
        UUID randomId = UUID.randomUUID();
        RequestSuspiciousActivityDTO updateRequest = new RequestSuspiciousActivityDTO(
                UUID.randomUUID(),
                "http://localhost:8080/updated",
                "10.0.0.1",
                "updated description"
        );
        assertThatThrownBy(() -> service.update(randomId, updateRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("SuspiciousActivity not found");
    }

    @Test
    @DisplayName("Should delete suspicious activity")
    public void testDeleteSuspiciousActivity() {
        var saved = service.save(request);

        service.delete(saved.id());

        assertThatThrownBy(() -> service.findById(saved.id()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw when deleting non-existent suspicious activity")
    public void testDeleteSuspiciousActivityNotFound() {
        UUID randomId = UUID.randomUUID();
        assertThatThrownBy(() -> service.delete(randomId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("SuspiciousActivity not found");
    }

    @Test
    @DisplayName("Should find all suspicious activities paginated")
    public void testFindAllSuspiciousActivities() {
        // Save multiple entities
        for (int i = 0; i < 5; i++) {
            SuspiciousActivity e = new SuspiciousActivity(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    "http://localhost:8080/resource" + i,
                    "192.168.1." + i,
                    "desc" + i,
                    Instant.now(),
                    Instant.now()
            );
            service.save(mapper.toDTO(e));
        }

        Pageable pageable = PageRequest.of(0, 3);
        Page<ResponseSuspiciousActivityDTO> page = service.findAll(pageable);

        assertThat(page).isNotNull();
        assertThat(page.getContent().size()).isLessThanOrEqualTo(3);
        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(5);
    }

    @Test
    @DisplayName("Should return empty page when no suspicious activities exist")
    public void testFindAllSuspiciousActivitiesEmpty() {
        repository.deleteAll();
        Pageable pageable = PageRequest.of(0, 5);
        Page<ResponseSuspiciousActivityDTO> page = service.findAll(pageable);

        assertThat(page).isNotNull();
        assertThat(page.getContent()).isEmpty();
        assertThat(page.getTotalElements()).isEqualTo(0);
    }
}
