package com.br.nobilesol.service.impl;

import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.ResetPasswordToken;
import com.br.nobilesol.exception.InvalidTokenException;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.exception.TokenExpiredException;
import com.br.nobilesol.repository.ResetPasswordTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class ResetPasswordService {

    private static final int RAW_BYTES = 48;
    private static final int EXPIRE_MINUTES = 10;
    private static final int COOLDOWN_MINUTES = 3;

    private final AccountService accountService;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    public ResetPasswordService(AccountService accountService,
                                ResetPasswordTokenRepository resetPasswordTokenRepository) {
        this.accountService = accountService;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }

    public String generateResetPassword(String anyCaseEmail) {
        String email = normalizeEmail(anyCaseEmail);
        Account account = accountService.findEntityByEmail(email);

        ResetPasswordToken last = resetPasswordTokenRepository.findTopByAccountOrderByCreatedAtDesc(account).orElse(null);
        if (last != null) {
            if (last.getUsedAt() == null && last.getExpiresAt().isAfter(Instant.now())) {
                Duration sinceCreation = Duration.between(last.getCreatedAt(), Instant.now());
                if (sinceCreation.compareTo(Duration.ofMinutes(COOLDOWN_MINUTES)) < 0) {
                    throw new NobileSolApiException(
                            "Aguarde alguns minutos para solicitar a recuperação novamente.",
                            HttpStatus.BAD_REQUEST
                    );
                }
            }
        }

        String raw = generateRawToken();
        String hash = sha256(raw);

        ResetPasswordToken t = new ResetPasswordToken();
        t.setAccount(account);
        t.setTokenHash(hash);
        t.setExpiresAt(Instant.now().plus(EXPIRE_MINUTES, ChronoUnit.MINUTES));

        resetPasswordTokenRepository.save(t);
        return raw;
    }

    public ResetPasswordToken validateResetPasswordToken(String rawToken) {
        String hash = sha256(rawToken);
        ResetPasswordToken token = resetPasswordTokenRepository.findByTokenHash(hash)
                .orElseThrow(InvalidTokenException::new);

        if (token.getUsedAt() != null) {
            throw new InvalidTokenException();
        }
        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new TokenExpiredException();
        }
        return token;
    }

    public void deleteResetPasswordToken(ResetPasswordToken token) {
        resetPasswordTokenRepository.delete(token);
    }

    public void markUsed(ResetPasswordToken token) {
        token.setUsedAt(Instant.now());
        resetPasswordTokenRepository.save(token);
    }

    @Scheduled(fixedRate = 86400000)
    public void deleteExpiredTokens() {
        List<ResetPasswordToken> expired = resetPasswordTokenRepository.findAllByExpiresAtBefore(Instant.now());
        if (!expired.isEmpty()) {
            resetPasswordTokenRepository.deleteAll(expired);
        }
    }

    private String normalizeEmail(String email) {
        if (email == null) return null;
        return email.trim().toLowerCase(Locale.ROOT);
    }

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
