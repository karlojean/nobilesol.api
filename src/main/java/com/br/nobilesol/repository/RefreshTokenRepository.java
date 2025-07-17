package com.br.nobilesol.repository;

import com.br.nobilesol.entity.RecoveryPasswordToken;
import com.br.nobilesol.entity.RefreshToken;
import com.br.nobilesol.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
    Optional<RefreshToken> findByUser(User user);
    List<RefreshToken> findAllByExpiryDateBefore(Instant expiryDate);
}
