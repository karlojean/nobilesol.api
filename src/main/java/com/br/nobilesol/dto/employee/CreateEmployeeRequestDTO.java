package com.br.nobilesol.dto.employee;

import com.br.nobilesol.dto.account.AccountRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateEmployeeRequestDTO(
        @NotNull
        @Valid
        AccountRequestDTO account,

        @NotEmpty
        String department
) {
}
