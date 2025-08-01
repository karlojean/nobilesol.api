package com.br.nobilesol.dto.auth;

import com.br.nobilesol.dto.account.AccountResponseDTO;

public record LoginResponseDTO(
        String accessToken,
        String refreshToken,
        AccountResponseDTO account
) {
}
