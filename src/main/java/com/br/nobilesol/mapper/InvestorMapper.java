package com.br.nobilesol.mapper;

import com.br.nobilesol.dto.account.AccountResponseDTO;
import com.br.nobilesol.dto.investor.CreateInvestorRequestDTO;
import com.br.nobilesol.dto.investor.InvestorResponseDTO;
import com.br.nobilesol.entity.Investor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InvestorMapper {
    @Mapping(target = "account", ignore = true)
    Investor toEntity(CreateInvestorRequestDTO createInvestorRequestDTO);

    @Mapping(target = "account", expression = "java(new AccountResponseDTO(investor.getAccount().getId(), investor.getFirstName(), investor.getAccount().getEmail(),  investor.getAccount().getRole()))")
    InvestorResponseDTO toResponseDTO(Investor investor);
}


