package com.br.nobilesol.dto.plant;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePlantRequestDTO(
    @NotNull UUID projectId,

    @NotEmpty @Size(min = 1, max = 255) String name,

    @NotEmpty @Size(max = 20) String type,

    @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal ratedPowerKw,

    @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal peakPowerKwp,

    @NotEmpty @Size(max = 20) String status) {
}