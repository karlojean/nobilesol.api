package com.br.nobilesol.listener;

import com.br.nobilesol.event.EmployeeCreatedEvent;
import com.br.nobilesol.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EmailNotificationListener {

    private final EmailService emailService;
    private final String frontendUrl;

    public EmailNotificationListener(
            EmailService emailService,
            @Value("${app.frontend.url:http://localhost:8080/default-login}") String frontendUrl
    ) {
        this.emailService = emailService;
        this.frontendUrl = frontendUrl;
    }

    @Async
    @EventListener
    public void handleEmployeeCreationEmail(EmployeeCreatedEvent event) {
        String loginUrl = frontendUrl + "/login";
        String subject = "Sua conta de acesso a plataforma da Nobile Sol foi criada!";
        String templateName = "welcome-email";

        Map<String, Object> variables = new HashMap<>();
        variables.put("platformName", "NobileSol");
        variables.put("employeeName", event.name());
        variables.put("employeeEmail", event.email());
        variables.put("tempPassword", event.temporaryPassword());
        variables.put("loginUrl", loginUrl);
        variables.put("companyName", "NobileSol Invest LTDA");
        variables.put("contactInfo", "jean@nobilesol.com.br");

        emailService.sendHtmlMessage(
                event.email(),
                subject,
                templateName,
                variables
        );
    }
}