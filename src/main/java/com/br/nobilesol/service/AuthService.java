package com.br.nobilesol.service;

import com.br.nobilesol.dto.auth.*;
import com.br.nobilesol.entity.RecoveryPasswordToken;
import com.br.nobilesol.entity.RefreshToken;
import com.br.nobilesol.entity.Account;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.utils.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final AccountService accountService;
    private final RecoveryPasswordService recoveryPasswordService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, AccountService accountService, RecoveryPasswordService recoveryPasswordService, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.accountService = accountService;
        this.recoveryPasswordService = recoveryPasswordService;
        this.refreshTokenService = refreshTokenService;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        Account accountPrincipal = (Account) authentication.getPrincipal();
        String jwt = jwtTokenUtil.generateToken(accountPrincipal);
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(accountPrincipal.getEmail());
        return new LoginResponseDTO(jwt, refreshToken.getToken());
    }

    public void sendRecoveryPasswordToken(ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
        RecoveryPasswordToken recoveryPasswordToken = recoveryPasswordService
                .generateRecoveryPassword(forgotPasswordRequestDTO.email());

        // Aqui tera o processo de envio por email, quando for implementado
        System.out.println(recoveryPasswordToken.getToken());
    }

    @Transactional
    public void resetPassword(ResetPasswordRequestDTO resetPasswordRequestDTO) {

       RecoveryPasswordToken validToken = recoveryPasswordService.validateRecoveryPasswordToken(resetPasswordRequestDTO.token());

        if (validToken == null) {
            throw new NobileSolApiException("Token Invalido", HttpStatus.BAD_REQUEST);
        }

        Account account = validToken.getAccount();

        accountService.changePassword(
                account,
                resetPasswordRequestDTO.password()
        );

        recoveryPasswordService.deleteRecoveryPasswordToken(validToken);
    }

    @Transactional
    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
         RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenRequestDTO.token());

         refreshTokenService.verifyExpiration(refreshToken);

         Account account =  refreshToken.getAccount();

         String accessToken = jwtTokenUtil.generateToken(account);

         refreshTokenService.deleteById(refreshToken.getId());
         RefreshToken newRefreshToken = refreshTokenService.generateRefreshToken(account.getEmail());

         return new RefreshTokenResponseDTO(accessToken, newRefreshToken.getToken());
    }

    @Transactional
    public void logout(Account account) {
        refreshTokenService.deleteTokenByAccount(account);
    }
}
