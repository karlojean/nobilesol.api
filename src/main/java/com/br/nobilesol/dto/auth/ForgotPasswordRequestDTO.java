package com.br.nobilesol.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(name = "ForgotPasswordRequest", description = "DTO for forgot password request")
public record ForgotPasswordRequestDTO (
        @NotNull
        @Email
        String email
) {
}
