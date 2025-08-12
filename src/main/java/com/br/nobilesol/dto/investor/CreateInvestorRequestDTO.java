package com.br.nobilesol.dto.investor;

import com.br.nobilesol.dto.account.AccountRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateInvestorRequestDTO(
        @NotNull
        @Valid
        AccountRequestDTO account,

        @NotEmpty
        @Size(min = 1, max = 50)
        String firstName,

        @NotEmpty
        @Size(min = 1, max = 255)
        String lastName,

        @NotEmpty
        String documentNumber,

        @Size(max = 11)
        String phoneNumber
) {
}
