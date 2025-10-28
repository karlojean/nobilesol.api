package com.br.nobilesol.dto.investor;

import com.br.nobilesol.dto.account.AccountRequestDTO;
import com.br.nobilesol.entity.enums.InvestorType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Schema(name = "CreateInvestorRequest", description = "DTO for creating a new investor")
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
