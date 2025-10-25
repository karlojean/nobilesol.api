package com.br.nobilesol.dto.project;

import com.br.nobilesol.entity.enums.ProjectStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateProjectRequestDTO(
    @NotEmpty
    @Size(min = 1, max = 255)
    String name,

    @Size(max = 1000)
    String description,

    @Digits(integer = 10, fraction = 8, message = "A latitude deve ter no máximo 10 dígitos inteiros e 8 dígitos fracionários")
    @DecimalMin(value = "-90.0", message = "A latitude mínima é -90.0")
    @DecimalMax(value = "90.0",  message = "A latitude máxima é 90.0")
    BigDecimal latitude,

    @Digits(integer = 11, fraction = 8, message = "A longitude deve ter no máximo 11 dígitos inteiros e 8 dígitos fracionários")
    @DecimalMin(value = "-180.0", message = "A longitude mínima é -180.0")
    @DecimalMax(value = "180.0", message = "A longitude máxima é 180.0")
    BigDecimal longitude,

    @NotNull
    @Positive(message = "A potência nominal deve ser um valor positivo")
    Long ratedPowerW,

    @NotNull
    @Positive(message = "A potência de pico deve ser um valor positivo")
    Long peakPowerWp,

    @NotEmpty @Size(max = 20)
    String businessModel,

    @Size(max = 255)
    String utilityCompany,

    @NotEmpty
    ProjectStatus status,

    LocalDate constructionStartDate,

    LocalDate commercialOperationDate,

    @Positive(message = "A geração anual esperada deve ser um valor positivo")
    Long expectedAnnualGenerationWh
) {
}