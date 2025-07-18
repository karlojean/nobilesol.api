package com.br.nobilesol.dto.employee;

import com.br.nobilesol.dto.user.UserResponseDTO;

import java.util.UUID;

public record EmployeeResponseDTO (
        UUID id,
        String department,
        UserResponseDTO user
) {
}
