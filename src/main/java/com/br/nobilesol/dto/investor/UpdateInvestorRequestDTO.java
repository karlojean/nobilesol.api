package com.br.nobilesol.dto.investor;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateInvestorRequestDTO(

        @Size(max = 255, message =  "Nome não pode exceder 255 caracteres")
        String name,

        @Size(max = 255, message = "Razão social não pode exceder 255 caracteres")
        String companyName,

        @Size(max = 255, message = "Nome fantasia não pode exceder 255 caracteres")
        String tradeName,

        @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10-11 digitos")
        String phoneNumber
) {
}

