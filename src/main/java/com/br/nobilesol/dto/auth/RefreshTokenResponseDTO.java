package com.br.nobilesol.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RefreshTokenResponse", description = "DTO for refresh token response")
public record RefreshTokenResponseDTO(
        String accessToken,
        String refreshToken
) {
}