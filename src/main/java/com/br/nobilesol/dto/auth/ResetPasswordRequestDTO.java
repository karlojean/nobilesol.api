package com.br.nobilesol.dto.auth;

import com.br.nobilesol.validation.anotations.PasswordMatches;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "ResetPasswordRequest", description = "DTO for reset password request")
@PasswordMatches
public record ResetPasswordRequestDTO(
        @NotNull
        String token,

        @NotNull
        String password,

        @NotNull
        String confirmPassword
) {
}
