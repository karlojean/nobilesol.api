package com.br.nobilesol.mapper;

import com.br.nobilesol.dto.investor.CreateInvestorRequestDTO;
import com.br.nobilesol.dto.investor.InvestorResponseDTO;
import com.br.nobilesol.dto.investor.UpdateInvestorRequestDTO;
import com.br.nobilesol.entity.Investor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {
        AccountMapper.class
})
public interface InvestorMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    Investor toEntity(CreateInvestorRequestDTO dto);

    InvestorResponseDTO toResponseDTO(Investor investor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "documentNumber", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateInvestorFromDto(UpdateInvestorRequestDTO request, @MappingTarget Investor investor);
}

