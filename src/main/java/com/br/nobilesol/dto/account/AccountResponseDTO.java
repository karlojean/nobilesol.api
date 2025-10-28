package com.br.nobilesol.dto.account;

import com.br.nobilesol.entity.enums.AccountRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(name = "AccountResponse", description = "DTO for account response")
public record AccountResponseDTO(
        UUID id,
        String email,
        AccountRole role,
        Boolean active,
        Instant createdAt
) {
}
