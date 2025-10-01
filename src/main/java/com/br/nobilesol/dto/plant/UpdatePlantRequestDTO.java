package com.br.nobilesol.dto.plant;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdatePlantRequestDTO(
    @Size(min = 1, max = 255, message = "Nome deve ter entre 1 e 255 caracteres") String name,

    @Size(max = 20, message = "Tipo não pode exceder 20 caracteres") String type,

    @DecimalMin(value = "0.0", inclusive = false, message = "Potência nominal deve ser maior que zero") BigDecimal ratedPowerKw,

    @DecimalMin(value = "0.0", inclusive = false, message = "Potência de pico deve ser maior que zero") BigDecimal peakPowerKwp,

    @Size(max = 20, message = "Status não pode exceder 20 caracteres") String status) {
}