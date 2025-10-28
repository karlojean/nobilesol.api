package com.br.nobilesol.dto.plant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(name = "UpdatePlantRequest", description = "DTO for updating an existing plant")
public record UpdatePlantRequestDTO(
    @Size(min = 1, max = 255, message = "Nome deve ter entre 1 e 255 caracteres")
    String name,

    @Size(max = 20, message = "Tipo não pode exceder 20 caracteres")
    String type,

    BigDecimal ratedPowerW,
    BigDecimal peakPowerWp,

    @Size(max = 20, message = "Status não pode exceder 20 caracteres")
    String status
) {
}