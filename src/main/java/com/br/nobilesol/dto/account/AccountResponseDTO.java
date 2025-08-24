package com.br.nobilesol.dto.account;

import com.br.nobilesol.entity.enums.AccountRole;

import java.time.Instant;
import java.util.UUID;

public record AccountResponseDTO(
        UUID id,
        String email,
        AccountRole role,
        Boolean isActive,
        Instant createdAt
) {
}
