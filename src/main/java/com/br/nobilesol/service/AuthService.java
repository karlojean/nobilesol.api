package com.br.nobilesol.service;

import com.br.nobilesol.dto.auth.*;
import com.br.nobilesol.entity.RecoveryPasswordToken;
import com.br.nobilesol.entity.RefreshToken;
import com.br.nobilesol.entity.User;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.utils.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final RecoveryPasswordService recoveryPasswordService;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService, RecoveryPasswordService recoveryPasswordService, RefreshTokenService refreshTokenService, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.recoveryPasswordService = recoveryPasswordService;
        this.refreshTokenService = refreshTokenService;
        this.userDetailsService = userDetailsService;
    }

    public LoginResponseDTO loginUser(LoginRequestDTO loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        User userPrincipal = (User) authentication.getPrincipal();
        String jwt = jwtTokenUtil.generateToken(userPrincipal);
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(userPrincipal.getEmail());
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

        User user = validToken.getUser();

        userService.changePassword(
                user,
                resetPasswordRequestDTO.password(),
                resetPasswordRequestDTO.confirmPassword()
        );

        recoveryPasswordService.deleteRecoveryPasswordToken(validToken);
    }

    @Transactional
    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
         RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenRequestDTO.token());

         refreshTokenService.verifyExpiration(refreshToken);

         User user =  refreshToken.getUser();

         String accessToken = jwtTokenUtil.generateToken(user);

         refreshTokenService.deleteById(refreshToken.getId());
         RefreshToken newRefreshToken = refreshTokenService.generateRefreshToken(user.getEmail());

         return new RefreshTokenResponseDTO(accessToken, newRefreshToken.getToken());
    }
}
