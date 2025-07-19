package com.br.nobilesol.repository;

import com.br.nobilesol.entity.RecoveryPasswordToken;
import com.br.nobilesol.entity.RefreshToken;
import com.br.nobilesol.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecoveryPasswordTokenRepository extends JpaRepository<RecoveryPasswordToken, UUID> {
    boolean existsByAccount(Account Account);
    RecoveryPasswordToken getByAccount(Account Account);
    Optional<RecoveryPasswordToken> getRecoveryPasswordTokenByToken(String token);
    List<RecoveryPasswordToken> findAllByExpiryDateBefore(Instant expiryDate);
}
