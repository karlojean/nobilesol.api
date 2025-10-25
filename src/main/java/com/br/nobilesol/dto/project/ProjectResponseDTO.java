package com.br.nobilesol.dto.project;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

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
    String projectStatus,
    LocalDate constructionStartDate,
    LocalDate commercialOperationDate,
    Long expectedAnnualGenerationWh,
    Instant createdAt,
    Instant updatedAt) {
}