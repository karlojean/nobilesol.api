package com.br.nobilesol.service.impl;

import com.br.nobilesol.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class SmtpEmailService implements EmailService {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    public SmtpEmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendHtmlMessage(String to, String subject, String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);

        try {
            String htmlContent = templateEngine.process(templateName, context);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail HTML", e);
        }
    }
}