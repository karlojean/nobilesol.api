package com.br.nobilesol.controller;

import com.br.nobilesol.dto.auth.ForgotPasswordRequestDTO;
import com.br.nobilesol.dto.auth.ResetPasswordRequestDTO;
import com.br.nobilesol.dto.auth.LoginRequestDTO;
import com.br.nobilesol.dto.auth.LoginResponseDTO;
import com.br.nobilesol.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO loginResponse = authService.loginUser(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
            @RequestBody @Valid ForgotPasswordRequestDTO forgotPasswordRequestDTO)
    {
        authService.sendRecoveryPasswordToken(forgotPasswordRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @RequestBody @Valid ResetPasswordRequestDTO resetPasswordRequestDTO
    ) {
        authService.resetPassword(resetPasswordRequestDTO);
        return ResponseEntity.ok().build();
    }


}
