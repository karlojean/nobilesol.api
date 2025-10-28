package com.br.nobilesol.dto.investor;

import com.br.nobilesol.dto.account.AccountResponseDTO;
import com.br.nobilesol.entity.enums.InvestorType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(name = "InvestorResponse", description = "DTO for investor response")
public record InvestorResponseDTO(
        UUID id,
        InvestorType type,
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
