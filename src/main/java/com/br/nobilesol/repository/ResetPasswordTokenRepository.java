package com.br.nobilesol.repository;

import com.br.nobilesol.entity.ResetPasswordToken;
import com.br.nobilesol.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, UUID> {
    boolean existsByAccount(Account Account);
    ResetPasswordToken getByAccount(Account Account);
    Optional<ResetPasswordToken> getResetPasswordTokenByToken(String token);
    List<ResetPasswordToken> findAllByExpiryDateBefore(Instant expiryDate);
}
