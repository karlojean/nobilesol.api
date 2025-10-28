package com.br.nobilesol.dto.employee;

import com.br.nobilesol.dto.account.AccountRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateEmployeeRequest", description = "DTO for creating a new employee")
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
