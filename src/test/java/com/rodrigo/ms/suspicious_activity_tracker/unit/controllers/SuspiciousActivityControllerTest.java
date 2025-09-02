package com.rodrigo.ms.suspicious_activity_tracker.unit.controllers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import static org.mockito.BDDMockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigo.ms.suspicious_activity_tracker.controller.SuspiciousActivityController;
import com.rodrigo.ms.suspicious_activity_tracker.dto.RequestSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.dto.ResponseSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.entities.SuspiciousActivity;
import com.rodrigo.ms.suspicious_activity_tracker.services.SuspiciousActivityService;

import jakarta.persistence.EntityNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SuspiciousActivityController.class)
public class SuspiciousActivityControllerTest {

    @MockitoBean
    private SuspiciousActivityService service;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    ResponseSuspiciousActivityDTO response;
    RequestSuspiciousActivityDTO request;
    SuspiciousActivity suspiciousActivity;

    @BeforeEach
    public void setup(){
        suspiciousActivity = new SuspiciousActivity(UUID.randomUUID(), UUID.randomUUID(), "https://api.meuservico.com/v1/resource", "192.168.1.2", "Suspicious activity", Instant.now(), Instant.now());
        request = new RequestSuspiciousActivityDTO(suspiciousActivity.getUserId(), suspiciousActivity.getEndpoint(), suspiciousActivity.getIpAddress(), suspiciousActivity.getDescription());

        response = new ResponseSuspiciousActivityDTO(suspiciousActivity.getId(), suspiciousActivity.getUserId(), suspiciousActivity.getEndpoint(), suspiciousActivity.getIpAddress(), suspiciousActivity.getDescription(), suspiciousActivity.getCreatedAt(), suspiciousActivity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should create new suspicious activity")
    public void testCreateSuspiciousActivity() throws Exception {
        // Arrange
        given(service.save(any(RequestSuspiciousActivityDTO.class))).willReturn(response);
        var json = objectMapper.writeValueAsString(request);
        // Act & Assert
        mvc.perform(post("/suspicious_activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/suspicious_activity/" + response.id()))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("Should return all suspicious activities with pagination")
    public void testFindAllSuspiciousActivities() throws Exception {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<ResponseSuspiciousActivityDTO> page = new PageImpl<>(List.of(response), pageable, 1);
        when(service.findAll(any(Pageable.class))).thenReturn(page);

        // Act & Assert
        mvc.perform(get("/suspicious_activity")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
    }

    @Test
    @DisplayName("Should find suspicious activity by id")
    public void testFindSuspiciousActivityById() throws Exception {
        // Arrange
        UUID id = suspiciousActivity.getId();
        when(service.findById(id)).thenReturn(response);

        // Act & Assert
        mvc.perform(get("/suspicious_activity/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("Should return 404 when suspicious activity not found")
    public void testFindSuspiciousActivityByIdNotFound() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        when(service.findById(id)).thenThrow(new EntityNotFoundException("SuspiciousActivity not found with id: " + id));

        // Act & Assert
        mvc.perform(get("/suspicious_activity/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should update suspicious activity")
    public void testUpdateSuspiciousActivity() throws Exception {
        // Arrange
        UUID id = suspiciousActivity.getId();
        when(service.update(eq(id), any(RequestSuspiciousActivityDTO.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(put("/suspicious_activity/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("Should return 404 when updating suspicious activity not found")
    public void testUpdateSuspiciousActivityNotFound() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        when(service.update(eq(id), any(RequestSuspiciousActivityDTO.class)))
                .thenThrow(new EntityNotFoundException("SuspiciousActivity not found with id: " + id));

        // Act & Assert
        mvc.perform(put("/suspicious_activity/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete suspicious activity")
    public void testDeleteSuspiciousActivity() throws Exception {
        // Arrange
        UUID id = suspiciousActivity.getId();
        BDDMockito.doNothing().when(service).delete(id);
        // Act & Assert
        mvc.perform(delete("/suspicious_activity/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 404 when deleting suspicious activity not found")
    public void testDeleteSuspiciousActivityNotFound() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        BDDMockito.doThrow(new EntityNotFoundException("SuspiciousActivity not found with id: " + id))
                .when(service).delete(id);

        // Act & Assert
        mvc.perform(delete("/suspicious_activity/" + id))
                .andExpect(status().isNotFound());
    }
}
