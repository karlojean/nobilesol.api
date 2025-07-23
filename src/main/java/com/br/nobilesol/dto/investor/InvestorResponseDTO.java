package com.br.nobilesol.dto.investor;

import com.br.nobilesol.dto.account.AccountResponseDTO;

import java.util.UUID;

public record InvestorResponseDTO(
        UUID id,
        String fullName,
        String documentNumber,
        String phoneNumber,
        AccountResponseDTO account
) {
}
