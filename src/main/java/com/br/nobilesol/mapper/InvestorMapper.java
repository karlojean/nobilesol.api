package com.br.nobilesol.mapper;

import com.br.nobilesol.dto.investor.CreateInvestorRequestDTO;
import com.br.nobilesol.dto.investor.InvestorResponseDTO;
import com.br.nobilesol.entity.Investor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        AccountMapper.class
})
public interface InvestorMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    Investor toEntity(CreateInvestorRequestDTO dto);

    InvestorResponseDTO toResponseDTO(Investor investor);
}

