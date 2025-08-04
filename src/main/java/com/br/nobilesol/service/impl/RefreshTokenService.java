package com.br.nobilesol.service.impl;

import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.RefreshToken;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountService accountService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, AccountService accountService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.accountService = accountService;
    }

    private static final long REFRESH_TOKEN_DURATION_MS = 24 * 60 * 60 * 1000;

    @Transactional
    public RefreshToken generateRefreshToken(String email) {
        Account account = accountService.findEntityByEmail(email);

        refreshTokenRepository.findByAccount(account)
                .ifPresent(refreshTokenRepository::delete);

        RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID().toString(),
                Instant.now().plusMillis(REFRESH_TOKEN_DURATION_MS),
                account
        );

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new NobileSolApiException("Refresh token não encontrada", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void deleteById(UUID id) {
        refreshTokenRepository.deleteById(id);
    }

    public void verifyExpiration(RefreshToken refreshToken) {
        if(refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new NobileSolApiException("Refresh token expirado, realize o login novamente", HttpStatus.UNAUTHORIZED);
        }
    }

    @Transactional
    public void deleteTokenByAccount(Account account) {
        refreshTokenRepository.deleteByAccount(account);
    }

    @Scheduled(fixedRate = 86400000)
    public void deleteExpiredTokens() {
        List<RefreshToken> expiredTokens = refreshTokenRepository.findAllByExpiryDateBefore(Instant.now());

        if (!expiredTokens.isEmpty()) {
            refreshTokenRepository.deleteAll(expiredTokens);
        }
    }
}
