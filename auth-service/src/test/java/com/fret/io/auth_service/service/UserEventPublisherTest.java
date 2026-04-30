package com.fret.io.auth_service.service;

import com.fret.io.auth_service.dto.RegisterRequest;
import com.fret.io.auth_service.dto.UserRegisteredEvent;
import com.fret.io.auth_service.model.DocumentType;
import com.fret.io.auth_service.model.User;
import com.fret.io.auth_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserEventPublisherTest {

    @InjectMocks
    UserEventPublisher userEventPublisher;

    @Mock
    RabbitTemplate rabbitTemplate;
    @Mock
    UserRepository userRepository;
    @Mock
    UserService userService;


    @Test
    void shouldPublishEventWhenUserIsDeactivatedTest(){
        UUID userId = UUID.randomUUID();
        String status = "user.deactivated.test";
        Instant time = Instant.now();

        userEventPublisher.publishUserDeactivated(userId,status);

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);

        verify(rabbitTemplate).convertAndSend(
                eq("user.events"),
                eq("user.deactivated"),
                captor.capture()
        );

        Map<String, Object> event = captor.getValue();

        assertEquals(userId, event.get("userId"));
        assertEquals(status, event.get("status"));
        assertTrue(event.get("timestamp") instanceof Instant);
    }

    @Test
    void shouldPublishEventWhenUserIsActivatedTest(){
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setEmail("test@gmail.com");
        user.setDocumentType(DocumentType.CPF);
        user.setDocument("111.000.111-70");

        userEventPublisher.publishUserRegistered(user);

        ArgumentCaptor<UserRegisteredEvent> captor =
                ArgumentCaptor.forClass(UserRegisteredEvent.class);

        verify(rabbitTemplate).convertAndSend(
                eq("user.events"),
                eq("user.registered"),
                captor.capture()
        );

        UserRegisteredEvent event = captor.getValue();

        assertEquals(userId,event.getIdUser());
        assertEquals(user.getEmail(), event.getEmail());
        assertEquals(user.getDocumentType(), event.getDocumentType());
        assertEquals(user.getDocument(), event.getDocument());
    }
}
