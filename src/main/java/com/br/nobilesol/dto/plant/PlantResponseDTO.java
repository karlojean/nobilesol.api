package com.br.nobilesol.dto.plant;


import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

public record PlantResponseDTO(
    UUID id,
    String name,
    String type,
    BigDecimal ratedPowerKw,
    BigDecimal peakPowerKwp,
    String status,
    Instant createdAt,
    Instant updatedAt) {
}