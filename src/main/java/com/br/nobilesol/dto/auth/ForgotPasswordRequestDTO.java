package com.br.nobilesol.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ForgotPasswordRequestDTO (
        @NotNull
        @Email
        String email
) {
}
