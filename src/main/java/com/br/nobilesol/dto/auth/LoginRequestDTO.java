package com.br.nobilesol.dto.auth;

import com.br.nobilesol.dto.auth.enums.PanelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO (
        @NotBlank(message = "O email deve ser obrigatório")
        String email,

        @NotBlank(message = "A senha deve ser obrigatória")
        String password,

        @NotNull
        PanelType panel
) {
}
