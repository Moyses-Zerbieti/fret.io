package com.fret.io.auth_service.service;

import com.fret.io.auth_service.dto.UserRegisteredEvent;
import com.fret.io.auth_service.model.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishUserDeactivated(UUID userId, String status){
        Map<String, Object> event = new HashMap<>();
        event.put("userId", userId);
        event.put("status", status);
        event.put("timestamp", Instant.now());

        rabbitTemplate.convertAndSend(
                "user.events",
                "user.deactivated",
                event
        );
    }

    public void publishUserRegistered(User user){
        UserRegisteredEvent event = new UserRegisteredEvent(
                    user.getId(),
                    user.getEmail(),
                    user.getDocumentType(),
                    user.getDocument()
        );

        rabbitTemplate.convertAndSend(
                "user.events",
                "user.registered",
                event
        );
    }
}

