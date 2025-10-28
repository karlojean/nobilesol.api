package com.br.nobilesol.controller;

import com.br.nobilesol.dto.auth.*;
import com.br.nobilesol.entity.Account;
import com.br.nobilesol.service.impl.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Endpoints for user authentication and authorization")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(operationId = "User login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @Operation(operationId = "Forgot password")
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
            @RequestBody @Valid ForgotPasswordRequestDTO forgotPasswordRequestDTO)
    {
        authService.sendResetPasswordToken(forgotPasswordRequestDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(operationId = "Reset password")
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @RequestBody @Valid ResetPasswordRequestDTO resetPasswordRequestDTO
    ) {
        authService.resetPassword(resetPasswordRequestDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(operationId = "Get new access token using refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @Operation(operationId = "User logout")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Account account) {
        authService.logout(account);
        return ResponseEntity.ok().build();
    }
}
