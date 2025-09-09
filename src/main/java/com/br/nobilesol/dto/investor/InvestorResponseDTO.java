package com.br.nobilesol.dto.investor;

import com.br.nobilesol.dto.account.AccountResponseDTO;
import com.br.nobilesol.entity.enums.InvestorType;

import java.time.Instant;
import java.util.UUID;

public record InvestorResponseDTO(
        UUID id,
        InvestorType investorType,
        String name,
        String companyName,
        String tradeName,
        String documentNumber,
        String phoneNumber,
        AccountResponseDTO account,
        Instant updatedAt,
        Instant createdAt
) {
}
