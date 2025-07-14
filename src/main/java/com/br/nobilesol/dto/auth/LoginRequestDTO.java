package com.br.nobilesol.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO (
        @NotBlank(message = "O email deve ser obrigatório")
        String email,

        @NotBlank(message = "A senha deve ser obrigatória")
        String password
) {
}
