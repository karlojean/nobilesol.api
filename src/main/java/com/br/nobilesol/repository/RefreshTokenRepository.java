package com.br.nobilesol.repository;

import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByAccount(Account user);
    Optional<RefreshToken> findByAccount(Account user);
    List<RefreshToken> findAllByExpiryDateBefore(Instant expiryDate);
}
