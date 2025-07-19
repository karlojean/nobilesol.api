package com.br.nobilesol.dto.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record AccountRequestDTO(
        @NotEmpty
        @Size(min = 3, max = 30)
        String name,

        @NotEmpty
        @Email
        String email
){
}
