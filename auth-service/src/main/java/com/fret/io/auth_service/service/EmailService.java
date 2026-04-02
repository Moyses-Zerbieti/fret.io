package com.fret.io.auth_service.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordReset (String forTo, String linkResetPassword){

        String resetLink = "http://localhost:8080/auth/reset-password/" + linkResetPassword;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(forTo);
        message.setSubject("Redefinição de senha");
        message.setText("Clique no link abaixo para redefinir sua senha:\n" +
                resetLink + "\n\n" +
                "O link expira em 1 hora"
        );

        mailSender.send(message);
    }
}
