package com.br.nobilesol.mapper;

import com.br.nobilesol.dto.investor.CreateInvestorRequestDTO;
import com.br.nobilesol.dto.investor.InvestorResponseDTO;
import com.br.nobilesol.dto.investor.UpdateInvestorRequestDTO;
import com.br.nobilesol.entity.Investor;
import com.br.nobilesol.entity.enums.InvestorType;
import jakarta.validation.ValidationException;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {AccountMapper.class}
)
public interface InvestorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    Investor toEntity(CreateInvestorRequestDTO dto);

    InvestorResponseDTO toResponseDTO(Investor investor);

    List<InvestorResponseDTO> toResponseDTOList(List<Investor> investors);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "documentNumber", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "account", ignore = true)
    void updateInvestorFromDTO(UpdateInvestorRequestDTO updateDTO, @MappingTarget Investor investor);

}

