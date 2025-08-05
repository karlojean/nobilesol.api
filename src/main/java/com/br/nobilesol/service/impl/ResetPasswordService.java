package com.br.nobilesol.service.impl;

import com.br.nobilesol.entity.ResetPasswordToken;
import com.br.nobilesol.entity.Account;
import com.br.nobilesol.exception.InvalidTokenException;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.exception.TokenExpiredException;
import com.br.nobilesol.repository.ResetPasswordTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ResetPasswordService {

    private static final int EXPIRE_TIME_TO_RECOVERY_MS = 60 * 1000 * 10;
    private static final int TIME_TO_NEW_REQUEST_MINUTES = 3;

    private final AccountService accountService;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    public ResetPasswordService(AccountService accountService, ResetPasswordTokenRepository resetPasswordTokenRepository) {
        this.accountService = accountService;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }

    public ResetPasswordToken generateResetPassword(String email) {
        Account account = accountService.findEntityByEmail(email);

        if (resetPasswordTokenRepository.existsByAccount(account)) {
            ResetPasswordToken lastResetPasswordToken = resetPasswordTokenRepository.getByAccount(account);

            if (lastResetPasswordToken.getExpiryDate().isBefore(Instant.now())){
                resetPasswordTokenRepository.delete(lastResetPasswordToken);
            } else {
                Duration creationTime = Duration.between(lastResetPasswordToken.getCreatedAt(), Instant.now());

                if (creationTime.compareTo(Duration.ofMinutes(TIME_TO_NEW_REQUEST_MINUTES)) < 0) {
                    throw new NobileSolApiException("Aguarde tres minutos para solicitar a recuperação de senha novamente.", HttpStatus.BAD_REQUEST);
                }
            }
        }

        ResetPasswordToken recoveryPasswordToken = new ResetPasswordToken();
        recoveryPasswordToken.setAccount(account);
        recoveryPasswordToken.setToken(UUID.randomUUID().toString());
        recoveryPasswordToken.setExpiryDate(Instant.now().plusMillis(EXPIRE_TIME_TO_RECOVERY_MS));

        resetPasswordTokenRepository.save(recoveryPasswordToken);

        return recoveryPasswordToken;
    }

    public ResetPasswordToken validateResetPasswordToken(String token) {
        ResetPasswordToken resetPassword = resetPasswordTokenRepository
                .getResetPasswordTokenByToken(token)
                .orElseThrow(InvalidTokenException::new);

        if (resetPassword.getExpiryDate().isBefore(Instant.now())) {
            throw new TokenExpiredException();
        }

        return resetPassword;
    }

    public void deleteResetPasswordToken(ResetPasswordToken token) {
        resetPasswordTokenRepository.delete(token);
    }

    @Scheduled(fixedRate = 86400000)
    public void deleteExpiredTokens() {
        List<ResetPasswordToken> expiredTokens = resetPasswordTokenRepository.findAllByExpiryDateBefore(Instant.now());

        if (!expiredTokens.isEmpty()) {
            resetPasswordTokenRepository.deleteAll(expiredTokens);
        }
    }
}
