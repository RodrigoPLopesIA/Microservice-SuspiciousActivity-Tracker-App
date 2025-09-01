package com.rodrigo.ms.suspicious_activity_tracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.rodrigo.ms.suspicious_activity_tracker.dto.RequestSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.dto.ResponseSuspiciousActivityDTO;
import com.rodrigo.ms.suspicious_activity_tracker.entities.SuspiciousActivity;

@Mapper(componentModel = "spring")
public interface SuspiciousActivityMapper {
    

    RequestSuspiciousActivityDTO toDTO(SuspiciousActivity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SuspiciousActivity toEntity(RequestSuspiciousActivityDTO dto);

    ResponseSuspiciousActivityDTO toResponseDTO(SuspiciousActivity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(RequestSuspiciousActivityDTO dto, @MappingTarget SuspiciousActivity entity);

    

}
