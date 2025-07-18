package com.br.nobilesol.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserCredentialsRequestDTO(
        @NotEmpty
        @Size(min = 3, max = 30)
        String name,

        @NotEmpty
        @Email
        String email
){
}
