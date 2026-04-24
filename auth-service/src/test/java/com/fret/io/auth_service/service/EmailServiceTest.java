package com.fret.io.auth_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {


    @InjectMocks
    EmailService emailService;

    @Mock
    JavaMailSender mailSender;


    @Test
    void sendEmailSucess(){
        String forTo = "test@gmail.com";
        String token = "tokenForReset";

        emailService.sendPasswordReset(forTo, token);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmailError(){
        String forTo ="test@gmail.com";
        String token = "tokenForReset";

        doThrow(new RuntimeException("SMTP ERROR"))
                .when(mailSender)
                .send(any(SimpleMailMessage.class));

        assertThrows(RuntimeException.class, ()->{
            emailService.sendPasswordReset(forTo,token);
        });
    }
}
