package com.br.nobilesol.dto.account;

import com.br.nobilesol.dto.employee.EmployeeResponseDTO;
import com.br.nobilesol.dto.investor.InvestorResponseDTO;
import com.br.nobilesol.entity.enums.AccountRole;
import lombok.NoArgsConstructor;

import java.util.UUID;

public record CurrentAccountResponseDTO(
        UUID id,
        String name,
        String email,
        AccountRole role,

        InvestorResponseDTO investor,
        EmployeeResponseDTO employee


) {

    public CurrentAccountResponseDTO(UUID id, String email, String name,AccountRole role) {
        this(id, name, email, role, null, null);
    }


}
