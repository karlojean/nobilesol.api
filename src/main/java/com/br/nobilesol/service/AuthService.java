package com.br.nobilesol.service;

import com.br.nobilesol.dto.auth.ForgotPasswordRequestDTO;
import com.br.nobilesol.dto.auth.LoginRequestDTO;
import com.br.nobilesol.dto.auth.LoginResponseDTO;
import com.br.nobilesol.dto.auth.ResetPasswordRequestDTO;
import com.br.nobilesol.entity.RecoveryPasswordToken;
import com.br.nobilesol.entity.User;
import com.br.nobilesol.exception.NobileSolApiException;
import com.br.nobilesol.utils.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final RecoveryPasswordService recoveryPasswordService;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService, RecoveryPasswordService recoveryPasswordService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.recoveryPasswordService = recoveryPasswordService;
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
        return new LoginResponseDTO(jwt);
    }

    public void sendRecoveryPasswordToken(ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
        RecoveryPasswordToken recoveryPasswordToken = recoveryPasswordService
                .generateRecoveryPassword(forgotPasswordRequestDTO.email());

        // Aqui tera o processo de envio por email, quando for implementado
        System.out.println(recoveryPasswordToken.getToken());
        return;
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
}
