package com.br.nobilesol.exception;

import org.springframework.http.HttpStatus;

public class TokenExpiredException extends NobileSolApiException {
    private static final String DEFAULT_MESSAGE = "Seu token de recuperação de senha expirou. Por favor, solicite um novo.";

    public TokenExpiredException() {
        super(DEFAULT_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    public TokenExpiredException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}