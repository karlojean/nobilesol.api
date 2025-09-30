package com.br.nobilesol.service.impl;

import com.br.nobilesol.dto.account.CurrentAccountResponseDTO;
import com.br.nobilesol.dto.auth.ForgotPasswordRequestDTO;
import com.br.nobilesol.dto.auth.LoginRequestDTO;
import com.br.nobilesol.dto.auth.LoginResponseDTO;
import com.br.nobilesol.dto.auth.RefreshTokenRequestDTO;
import com.br.nobilesol.dto.auth.RefreshTokenResponseDTO;
import com.br.nobilesol.dto.auth.ResetPasswordRequestDTO;
import com.br.nobilesol.dto.auth.enums.PanelType;
import com.br.nobilesol.entity.Account;
import com.br.nobilesol.entity.RefreshToken;
import com.br.nobilesol.entity.ResetPasswordToken;
import com.br.nobilesol.entity.enums.AccountRole;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.mapper.EmployeeMapper;
import com.br.nobilesol.mapper.InvestorMapper;
import com.br.nobilesol.utils.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final AccountService accountService;
    private final ResetPasswordService resetPasswordService;
    private final RefreshTokenService refreshTokenService;
    private final InvestorMapper investorMapper;
    private final EmployeeMapper employeeMapper;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtTokenUtil jwtTokenUtil,
            AccountService accountService,
            ResetPasswordService resetPasswordService,
            RefreshTokenService refreshTokenService,
            InvestorMapper investorMapper, EmployeeMapper employeeMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.accountService = accountService;
        this.resetPasswordService = resetPasswordService;
        this.refreshTokenService = refreshTokenService;
        this.investorMapper = investorMapper;
        this.employeeMapper = employeeMapper;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        String email = normalizeEmail(loginRequest.email());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, loginRequest.password())
        );

        Account accountPrincipal = (Account) authentication.getPrincipal();

        validatePanelAccess(loginRequest.panel(), accountPrincipal.getRole());

        String jwt = jwtTokenUtil.generateToken(accountPrincipal);

        String rawRefreshToken = refreshTokenService.generateRefreshToken(accountPrincipal);

        String displayName = accountService.resolveDisplayName(accountPrincipal);

        CurrentAccountResponseDTO currentAccount = new CurrentAccountResponseDTO(
                accountPrincipal.getId(),
                displayName,
                accountPrincipal.getEmail(),
                accountPrincipal.getRole(),
                accountPrincipal.getInvestor() != null ? investorMapper.toResponseDTO(accountPrincipal.getInvestor()) : null,
                accountPrincipal.getEmployee() != null ? employeeMapper.toResponseDTO(accountPrincipal.getEmployee()) : null
        );

        return new LoginResponseDTO(jwt, rawRefreshToken, currentAccount);
    }

    public void sendResetPasswordToken(ForgotPasswordRequestDTO dto) {
        String email = normalizeEmail(dto.email());


        String rawToken = resetPasswordService.generateResetPassword(email);

        // TODO: enviar por e-mail (usar SmtpEmailService)
        // Ex.: emailService.sendResetLink(email, rawToken);
        // (Evite logar o raw token em produção)
        System.out.println("[DEBUG] reset token (RAW): " + rawToken);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequestDTO dto) {
        ResetPasswordToken validToken = resetPasswordService.validateResetPasswordToken(dto.token());
        if (validToken == null) {
            throw new NobileSolApiException("Token inválido", HttpStatus.BAD_REQUEST);
        }

        Account account = validToken.getAccount();
        accountService.changePassword(account, dto.password());

        resetPasswordService.deleteResetPasswordToken(validToken);
    }

    // ========= REFRESH TOKEN =========
    @Transactional
    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO dto) {
        RefreshToken stored = refreshTokenService.validateByRawToken(dto.token());

        Account account = stored.getAccount();

        String newAccessToken = jwtTokenUtil.generateToken(account);

        refreshTokenService.revoke(stored);
        String newRawRefreshToken = refreshTokenService.generateRefreshToken(account);

        return new RefreshTokenResponseDTO(newAccessToken, newRawRefreshToken);
    }

    @Transactional
    public void logout(Account account) {
        refreshTokenService.revokeAllByAccount(account);
    }

    private String normalizeEmail(String email) {
        if (email == null) return null;
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private void validatePanelAccess(PanelType requestedPanel, AccountRole userRole) {
        if (requestedPanel == PanelType.EMPLOYEE) {
            if (userRole != AccountRole.EMPLOYEE) {
                throw new NobileSolApiException(
                        "Acesso negado. Apenas investidores podem acessar este painel.",
                        HttpStatus.FORBIDDEN
                );
            }
        } else if (requestedPanel == PanelType.INVESTOR) {
            if (userRole != AccountRole.INVESTOR) {
                throw new NobileSolApiException(
                        "Acesso negado. Apenas investidores podem acessar este painel.",
                        HttpStatus.FORBIDDEN
                );
            }
        }
    }
}
