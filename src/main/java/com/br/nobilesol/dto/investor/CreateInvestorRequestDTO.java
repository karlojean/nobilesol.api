package com.br.nobilesol.dto.investor;

import com.br.nobilesol.dto.account.AccountRequestDTO;
import com.br.nobilesol.entity.enums.InvestorType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateInvestorRequestDTO(
        @NotNull(message = "O tipo de investidor (PF ou PJ) é obrigatório")
        InvestorType type,

        @NotNull(message = "O número do documento é obrigatório")
        String documentNumber,
        String name,
        String companyName,
        String tradeName,
        String phoneNumber,

        @NotNull @Valid
        AccountRequestDTO account
) {
}
