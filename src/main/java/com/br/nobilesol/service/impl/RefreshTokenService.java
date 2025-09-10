package com.br.nobilesol.service.impl;

import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.RefreshToken;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;

@Service
public class RefreshTokenService {

    private static final int RAW_BYTES = 64;                 // robusto
    private static final int TTL_DAYS = 30;                  // ajuste conforme política

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public String generateRefreshToken(Account account) {
        refreshTokenRepository.revokeAllActiveByAccountId(account.getId(), Instant.now());

        String raw = generateRawToken();
        String hash = sha256(raw);

        RefreshToken rt = new RefreshToken();
        rt.setAccount(account);
        rt.setTokenHash(hash);
        rt.setExpiresAt(Instant.now().plus(TTL_DAYS, ChronoUnit.DAYS));

        refreshTokenRepository.save(rt);
        return raw;
    }

    public RefreshToken validateByRawToken(String rawToken) {
        String hash = sha256(rawToken);
        RefreshToken rt = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new NobileSolApiException("Refresh token inválido", HttpStatus.UNAUTHORIZED));

        if (rt.getRevokedAt() != null) {
            throw new NobileSolApiException("Refresh token revogado", HttpStatus.UNAUTHORIZED);
        }
        if (rt.getExpiresAt().isBefore(Instant.now())) {
            throw new NobileSolApiException("Refresh token expirado, faça login novamente", HttpStatus.UNAUTHORIZED);
        }
        return rt;
    }

    @Transactional
    public void revoke(RefreshToken token) {
        token.setRevokedAt(Instant.now());
        refreshTokenRepository.save(token);
    }

    @Transactional
    public void revokeAllByAccount(Account account) {
        refreshTokenRepository.revokeAllActiveByAccountId(account.getId(), Instant.now());
    }

    @Scheduled(fixedRate = 86400000)
    public void deleteExpiredTokens() {
        List<RefreshToken> expired = refreshTokenRepository.findAllByExpiresAtBefore(Instant.now());
        if (!expired.isEmpty()) {
            refreshTokenRepository.deleteAll(expired);
        }
    }

    // -------- helpers --------

    private String generateRawToken() {
        byte[] buf = new byte[RAW_BYTES];
        new SecureRandom().nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }

    private String sha256(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] out = md.digest(raw.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(out);
        } catch (Exception e) {
            throw new IllegalStateException("SHA-256 unavailable", e);
        }
    }
}
