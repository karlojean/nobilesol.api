package com.br.nobilesol.repository;

import com.br.nobilesol.entity.RecoveryPasswordToken;
import com.br.nobilesol.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RecoveryPasswordTokenRepository extends JpaRepository<RecoveryPasswordToken, UUID> {
    boolean existsByUser(User user);
    RecoveryPasswordToken getByUser(User user);

    Optional<RecoveryPasswordToken> getRecoveryPasswordTokenByToken(String token);
}
