package com.br.nobilesol.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

@Component
public class DelegatedAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "É necessário um token de autenticação válido para acessar este recurso."
        );


        problemDetail.setTitle("Não autorizado");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), problemDetail);
    }
}
