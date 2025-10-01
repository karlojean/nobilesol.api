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
    BigDecimal ratedPowerKw,
    BigDecimal peakPowerKwp,
    String businessModel,
    String energyDistributionRule,
    String utilityCompany,
    String projectStatus,
    LocalDate constructionStartDate,
    LocalDate commercialOperationDate,
    BigDecimal expectedAnnualGenerationMwh,
    Instant createdAt,
    Instant updatedAt) {
}