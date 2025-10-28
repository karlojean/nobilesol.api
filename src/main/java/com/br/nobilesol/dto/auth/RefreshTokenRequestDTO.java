package com.br.nobilesol.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "RefreshTokenRequest", description = "DTO for refresh token request")
public record RefreshTokenRequestDTO (
        @NotNull
        String token
) {

}