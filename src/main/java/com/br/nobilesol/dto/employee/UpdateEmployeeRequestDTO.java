package com.br.nobilesol.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateEmployeeRequest", description = "DTO for updating an existing employee")
public record UpdateEmployeeRequestDTO(
    @Size(min = 3, max = 255, message = "Nome deve ter entre 3 e 255 caracteres")
    String name,

    @Size(max = 100, message = "Departamento não pode exceder 100 caracteres")
    String department,

    Boolean isAdmin
) {
}