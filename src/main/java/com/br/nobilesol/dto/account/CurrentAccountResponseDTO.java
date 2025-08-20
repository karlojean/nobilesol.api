package com.br.nobilesol.dto.account;

import com.br.nobilesol.entity.enums.AccountRole;

import java.util.UUID;

public record CurrentAccountResponseDTO (
        UUID id,
        String name,
        String email,
        AccountRole role
) {
}
