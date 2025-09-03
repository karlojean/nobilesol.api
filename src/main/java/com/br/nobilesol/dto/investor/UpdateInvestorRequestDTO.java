package com.br.nobilesol.dto.investor;

public record UpdateInvestorRequestDTO(
        String name,
        String companyName,
        String tradeName,
        String phoneNumber
) {
}
