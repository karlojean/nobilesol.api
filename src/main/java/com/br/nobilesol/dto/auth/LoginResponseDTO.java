package com.br.nobilesol.dto.auth;

public record LoginResponseDTO(
        String accessToken,
        String refreshToken
) {
}
