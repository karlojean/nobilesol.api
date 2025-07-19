package com.br.nobilesol.mapper;

import com.br.nobilesol.dto.account.AccountResponseDTO;
import com.br.nobilesol.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponseDTO toResponseDTO(Account account);
}
