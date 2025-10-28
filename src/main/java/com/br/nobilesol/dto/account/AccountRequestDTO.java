package com.br.nobilesol.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Schema(name = "AccountRequest", description = "DTO for account request")
public record AccountRequestDTO(
        @NotEmpty
        @Email
        String email
){
}
