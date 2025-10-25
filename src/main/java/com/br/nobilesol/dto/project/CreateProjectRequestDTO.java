package com.br.nobilesol.dto.project;

import jakarta.validation.constraints.*;

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
    @Positive(message = "A potência nominal deve ser um valor positivo")
    Long ratedPowerW,

    @NotNull
    @Positive(message = "A potência de pico deve ser um valor positivo")
    Long peakPowerWp,

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

    @Positive(message = "A geração anual esperada deve ser um valor positivo")
    Long expectedAnnualGenerationWh) {
}