package com.br.nobilesol.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class NobileSolApiException extends RuntimeException {

    private final HttpStatus status;

    public NobileSolApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(this.status);
        pb.setTitle("Erro na API Nobile Sol");
        pb.setDetail(this.getMessage());
        return pb;
    }
}