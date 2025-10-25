package com.br.nobilesol.dto.project;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateProjectRequestDTO(
    @Size(min = 1, max = 255, message = "Nome deve ter entre 1 e 255 caracteres")
    String name,

    @Size(max = 1000, message = "Descrição não pode exceder 1000 caracteres")
    String description,

    BigDecimal latitude,

    BigDecimal longitude,

    Long ratedPowerW,

    Long peakPowerWp,

    @Size(max = 20, message = "Modelo de negócio não pode exceder 20 caracteres")
    String businessModel,

    @Size(max = 255, message = "Concessionária não pode exceder 255 caracteres")
    String utilityCompany,

    @Size(max = 20, message = "Status do projeto não pode exceder 20 caracteres")
    String projectStatus,

    LocalDate constructionStartDate,

    LocalDate commercialOperationDate,

    Long expectedAnnualGenerationWh) {
}