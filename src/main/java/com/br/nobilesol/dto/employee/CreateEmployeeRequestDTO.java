package com.br.nobilesol.dto.employee;

import com.br.nobilesol.dto.account.AccountRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateEmployeeRequestDTO(
        @NotEmpty
        @Size(min = 1, max = 255)
        String name,

        @NotEmpty
        String department,

        boolean isAdmin,

        @NotNull
        @Valid
        AccountRequestDTO account
) {
}
