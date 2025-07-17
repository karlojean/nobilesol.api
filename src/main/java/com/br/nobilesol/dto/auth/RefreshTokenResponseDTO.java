package com.br.nobilesol.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public record RefreshTokenResponseDTO(
        String accessToken,
        String refreshToken
) {
}