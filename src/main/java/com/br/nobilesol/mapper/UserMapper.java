package com.br.nobilesol.mapper;

import com.br.nobilesol.dto.user.UserResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toResponseDTO(UserResponseDTO userResponseDTO);
}
