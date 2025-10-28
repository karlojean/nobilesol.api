package com.br.nobilesol.dto.project;

import com.br.nobilesol.entity.enums.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "ProjectResponse", description = "DTO for project response")
public record ProjectResponseDTO(
    UUID id,
    String name,
    String description,
    BigDecimal latitude,
    BigDecimal longitude,
    Long ratedPowerW,
    Long peakPowerWp,
    String businessModel,
    String utilityCompany,
    ProjectStatus status,
    LocalDate constructionStartDate,
    LocalDate commercialOperationDate,
    Long expectedAnnualGenerationWh,
    Instant createdAt,
    Instant updatedAt) {
}