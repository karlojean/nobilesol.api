package com.br.nobilesol.service;

import com.br.nobilesol.entity.RecoveryPasswordToken;
import com.br.nobilesol.entity.RefreshToken;
import com.br.nobilesol.entity.User;
import com.br.nobilesol.exception.InvalidTokenException;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.exception.TokenExpiredException;
import com.br.nobilesol.repository.RecoveryPasswordTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecoveryPasswordService {

    private static final int EXPIRE_TIME_TO_RECOVERY_MS = 60 * 1000 * 10;
    private static final int TIME_TO_NEW_REQUEST_MINUTES = 3;

    private final UserService userService;
    private final RecoveryPasswordTokenRepository recoveryPasswordTokenRepository;

    public RecoveryPasswordService(UserService userService, RecoveryPasswordTokenRepository recoveryPasswordTokenRepository) {
        this.userService = userService;
        this.recoveryPasswordTokenRepository = recoveryPasswordTokenRepository;
    }

    public RecoveryPasswordToken generateRecoveryPassword(String email) {
        User user = userService.findEntityByEmail(email);

        if (recoveryPasswordTokenRepository.existsByUser(user)) {
            RecoveryPasswordToken lastRecoveryPasswordToken = recoveryPasswordTokenRepository.getByUser(user);

            if (lastRecoveryPasswordToken.getExpiryDate().isBefore(Instant.now())){
                recoveryPasswordTokenRepository.delete(lastRecoveryPasswordToken);
            } else {
                Duration creationTime = Duration.between(lastRecoveryPasswordToken.getCreatedAt(), Instant.now());

                if (creationTime.compareTo(Duration.ofMinutes(TIME_TO_NEW_REQUEST_MINUTES)) < 0) {
                    throw new NobileSolApiException("Aguarde tres minutos para solicitar a recuperação de senha novamente.", HttpStatus.BAD_REQUEST);
                }
            }
        }

        RecoveryPasswordToken recoveryPasswordToken = new RecoveryPasswordToken();
        recoveryPasswordToken.setUser(user);
        recoveryPasswordToken.setToken(UUID.randomUUID().toString());
        recoveryPasswordToken.setExpiryDate(Instant.now().plusMillis(EXPIRE_TIME_TO_RECOVERY_MS));

        recoveryPasswordTokenRepository.save(recoveryPasswordToken);

        return recoveryPasswordToken;
    }

    public RecoveryPasswordToken validateRecoveryPasswordToken(String token) {
        RecoveryPasswordToken recoveryToken = recoveryPasswordTokenRepository
                .getRecoveryPasswordTokenByToken(token)
                .orElseThrow(InvalidTokenException::new);

        if (recoveryToken.getExpiryDate().isBefore(Instant.now())) {
            throw new TokenExpiredException();
        }

        return recoveryToken;
    }

    public void deleteRecoveryPasswordToken(RecoveryPasswordToken token) {
        recoveryPasswordTokenRepository.delete(token);
    }

    @Scheduled(fixedRate = 86400000)
    public void deleteExpiredTokens() {
        List<RecoveryPasswordToken> expiredTokens = recoveryPasswordTokenRepository.findAllByExpiryDateBefore(Instant.now());

        if (!expiredTokens.isEmpty()) {
            recoveryPasswordTokenRepository.deleteAll(expiredTokens);
        }
    }
}
