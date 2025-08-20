package com.br.nobilesol.dto.auth;

import com.br.nobilesol.dto.account.CurrentAccountResponseDTO;

public record LoginResponseDTO(
        String accessToken,
        String refreshToken,
        CurrentAccountResponseDTO account
) {
}
