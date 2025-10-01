package com.br.nobilesol.dto.project;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateProjectRequestDTO(
    @NotEmpty @Size(min = 1, max = 255)
    String name,

    @Size(max = 1000)
    String description,

    BigDecimal latitude,

    BigDecimal longitude,

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal ratedPowerKw,

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal peakPowerKwp,

    @NotEmpty @Size(max = 20)
    String businessModel,

    @Size(max = 20)
    String energyDistributionRule,

    @Size(max = 255)
    String utilityCompany,

    @NotEmpty @Size(max = 20)
    String projectStatus,

    LocalDate constructionStartDate,

    LocalDate commercialOperationDate,

    @DecimalMin(value = "0.0")
    BigDecimal expectedAnnualGenerationMwh) {
}