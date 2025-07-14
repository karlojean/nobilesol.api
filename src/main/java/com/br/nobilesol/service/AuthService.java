package com.br.nobilesol.service;

import com.br.nobilesol.dto.auth.LoginRequestDTO;
import com.br.nobilesol.dto.auth.LoginResponseDTO;
import com.br.nobilesol.entity.User;
import com.br.nobilesol.utils.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
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
}
