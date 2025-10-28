package com.br.nobilesol.dto.employee;

import com.br.nobilesol.dto.account.AccountResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "EmployeeResponse", description = "DTO for employee response")
public record EmployeeResponseDTO (
        UUID id,
        String name,
        String department,
        Boolean admin,
        AccountResponseDTO account
) {
}
