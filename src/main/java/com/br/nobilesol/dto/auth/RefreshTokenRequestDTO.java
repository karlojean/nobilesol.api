package com.br.nobilesol.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record RefreshTokenRequestDTO (
        @NotNull
        String token
) {

}