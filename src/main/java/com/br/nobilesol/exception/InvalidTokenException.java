package com.br.nobilesol.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends NobileSolApiException {

    private static final String DEFAULT_MESSAGE = "Token de recuperação inválido ou não encontrado.";

    public InvalidTokenException() {
        super(DEFAULT_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    public InvalidTokenException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

