package com.br.nobilesol.dto.auth;

import com.br.nobilesol.dto.account.CurrentAccountResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginResponse", description = "DTO for login response")
public record LoginResponseDTO(
        String accessToken,
        String refreshToken,
        CurrentAccountResponseDTO account
) {
}
