package com.br.nobilesol.dto.plant;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(name = "PlantResponse", description = "DTO for plant response")
public record PlantResponseDTO(
    UUID id,
    String name,
    String type,
    Long ratedPowerW,
    Long peakPowerWp,
    String status,
    Instant createdAt,
    Instant updatedAt) {
}