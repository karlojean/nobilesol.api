package com.br.nobilesol.dto.account;

import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.enums.AccountRole;

import java.util.UUID;

public record AccountResponseDTO(
        UUID id,
        String name,
        String email,
        AccountRole role
) {
}
