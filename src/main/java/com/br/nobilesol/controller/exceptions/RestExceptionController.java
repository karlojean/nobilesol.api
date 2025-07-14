package com.br.nobilesol.controller.exceptions;

import com.br.nobilesol.exception.NobileSolApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class RestExceptionController {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception e) {
        var pb = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pb.setTitle("Erro Inesperado");
        pb.setDetail("Ocorreu um erro inesperado no servidor. Por favor, tente novamente mais tarde.");

        return pb;
    }

    @ExceptionHandler(value = NobileSolApiException.class)
    public ProblemDetail handleNobileSolApiException(NobileSolApiException e) {
        return e.toProblemDetail();
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<InvalidParam> fieldErrors = e.getFieldErrors().stream().map(f -> new InvalidParam(f.getField(), f.getDefaultMessage())).toList() ;
        var pb = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Campos inválidos");
        pb.setProperty("invalid-params", fieldErrors);

        return pb;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException(BadCredentialsException e) {
        var pb = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);

        pb.setTitle("Credenciais Inválidas");
        pb.setDetail("O nome de usuário ou a senha fornecidos estão incorretos.");
        pb.setProperty("timestamp", Instant.now());

        return pb;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException e) {
        var pb = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);

        pb.setTitle("Acesso Negado");
        pb.setDetail("Você não tem permissão para acessar este recurso.");
        pb.setProperty("timestamp", Instant.now());

        return pb;
    }

    private record InvalidParam(String name, String reason){};
}
