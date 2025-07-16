package com.br.nobilesol.dto.auth;

import com.br.nobilesol.validation.anotations.PasswordMatches;
import jakarta.validation.constraints.NotNull;

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
