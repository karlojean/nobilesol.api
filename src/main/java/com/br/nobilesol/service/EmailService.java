package com.br.nobilesol.service;

import java.util.Map;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendHtmlMessage(String to, String subject, String templateName, Map<String, Object> variables);
}