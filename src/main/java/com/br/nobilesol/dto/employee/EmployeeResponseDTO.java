package com.br.nobilesol.dto.employee;

import com.br.nobilesol.dto.account.AccountResponseDTO;

import java.util.UUID;

public record EmployeeResponseDTO (
        UUID id,
        String firstName,
        String lastName,
        String department,
        AccountResponseDTO account
) {
}
