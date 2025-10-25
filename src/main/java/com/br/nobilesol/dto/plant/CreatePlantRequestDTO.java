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

    @NotNull Long ratedPowerW,

    @NotNull Long peakPowerWp,

    @NotEmpty @Size(max = 20) String status) {
}