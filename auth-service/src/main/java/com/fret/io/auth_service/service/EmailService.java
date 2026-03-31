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

    public void sendPasswordReset (String forTo, String hash){


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(forTo);
        message.setSubject("Redefinição de senha");
        message.setText("Código para redefinir sua senha: " + hash);

        mailSender.send(message);
    }
}
