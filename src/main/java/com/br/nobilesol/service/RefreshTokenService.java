package com.br.nobilesol.service;

import com.br.nobilesol.entity.RefreshToken;
import com.br.nobilesol.entity.User;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.repository.RefreshTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserService userService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    private static final long REFRESH_TOKEN_DURATION_MS = 24 * 60 * 60 * 1000;

    public RefreshToken generateRefreshToken(String email) {
        User user = userService.findEntityByEmail(email);

        refreshTokenRepository.findByUser(user)
                .ifPresent(refreshTokenRepository::delete);

        RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID().toString(),
                Instant.now().plusMillis(REFRESH_TOKEN_DURATION_MS),
                user
        );

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new NobileSolApiException("Refresh token não encontrada", HttpStatus.NOT_FOUND));
    }

    public void deleteById(UUID id) {
        refreshTokenRepository.deleteById(id);
    }

    public void verifyExpiration(RefreshToken refreshToken) {
        if(refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new NobileSolApiException("Refresh token expirado, realize o login novamente", HttpStatus.UNAUTHORIZED);
        }
    }

    @Scheduled(fixedRate = 86400000)
    public void deleteExpiredTokens() {
        List<RefreshToken> expiredTokens = refreshTokenRepository.findAllByExpiryDateBefore(Instant.now());

        if (!expiredTokens.isEmpty()) {
            refreshTokenRepository.deleteAll(expiredTokens);
        }
    }
}
