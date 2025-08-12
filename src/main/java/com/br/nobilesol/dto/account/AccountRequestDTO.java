package com.br.nobilesol.dto.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record AccountRequestDTO(
        @NotEmpty
        @Email
        String email
){
}
