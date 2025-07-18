package com.br.nobilesol.dto.user;

import com.br.nobilesol.entity.enums.UserRole;

import java.util.UUID;

public record UserResponseDTO (
        UUID id,
        String name,
        String email,
        UserRole role
) {
}
