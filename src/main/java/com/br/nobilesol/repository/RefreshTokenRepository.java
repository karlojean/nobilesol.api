package com.br.nobilesol.repository;

import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    @Modifying
    @Query("""
        UPDATE RefreshToken rt
           SET rt.revokedAt = :revokedAt
         WHERE rt.account.id = :accountId
           AND rt.revokedAt IS NULL
    """)
    int revokeAllActiveByAccountId(@Param("accountId") UUID accountId,
                                   @Param("revokedAt") Instant revokedAt);

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findAllByExpiresAtBefore(Instant now);
}