package com.br.nobilesol.service.impl;

import com.br.nobilesol.dto.auth.*;
import com.br.nobilesol.dto.auth.enums.PanelType;
import com.br.nobilesol.entity.ResetPasswordToken;
import com.br.nobilesol.entity.RefreshToken;
import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.enums.AccountRole;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.mapper.AccountMapper;
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
    private final ResetPasswordService resetPasswordService;
    private final RefreshTokenService refreshTokenService;
    private final AccountMapper accountMapper;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, AccountService accountService, ResetPasswordService resetPasswordService, RefreshTokenService refreshTokenService, AccountMapper accountMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.accountService = accountService;
        this.resetPasswordService = resetPasswordService;
        this.refreshTokenService = refreshTokenService;
        this.accountMapper = accountMapper;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        Account accountPrincipal = (Account) authentication.getPrincipal();
        validatePanelAccess(loginRequest.panel(), accountPrincipal.getRole());
        String jwt = jwtTokenUtil.generateToken(accountPrincipal);
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(accountPrincipal.getEmail());

        return new LoginResponseDTO(jwt, refreshToken.getToken(), accountMapper.toResponseDTO(accountPrincipal));
    }

    public void sendResetPasswordToken(ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
        ResetPasswordToken resetPasswordToken = resetPasswordService
                .generateResetPassword(forgotPasswordRequestDTO.email());

        // Aqui tera o processo de envio por email, quando for implementado
        System.out.println(resetPasswordToken.getToken());
    }

    @Transactional
    public void resetPassword(ResetPasswordRequestDTO resetPasswordRequestDTO) {

       ResetPasswordToken validToken = resetPasswordService.validateResetPasswordToken(resetPasswordRequestDTO.token());

        if (validToken == null) {
            throw new NobileSolApiException("Token Invalido", HttpStatus.BAD_REQUEST);
        }

        Account account = validToken.getAccount();

        accountService.changePassword(
                account,
                resetPasswordRequestDTO.password()
        );

        resetPasswordService.deleteResetPasswordToken(validToken);
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

    private void validatePanelAccess(PanelType requestedPanel, AccountRole userRole) {
        if (requestedPanel == PanelType.EMPLOYEE && userRole != AccountRole.EMPLOYEE) {
            throw new NobileSolApiException("Acesso negado. Apenas funcionários podem aceder a este painel.", HttpStatus.FORBIDDEN);
        }
        if (requestedPanel == PanelType.INVESTOR && userRole != AccountRole.INVESTOR) {
            throw new NobileSolApiException("Acesso negado. Apenas investidores podem aceder a este painel.", HttpStatus.FORBIDDEN);
        }
    }
}
