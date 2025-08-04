package org.example.notificationservice.kafka;

import org.example.notificationservice.dto.UserEvent;
import org.example.notificationservice.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UserEventsConsumer {

    private final EmailService emailService;

    public UserEventsConsumer(EmailService emailService) {
        this.emailService = emailService;
    }


    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void handleUserEvents(@Payload UserEvent event) {
        if ("CREATE".equals(event.getEventType())) {
            emailService.sendUserCreatedEmail(event.getEmail());
        } else if ("DELETE".equals(event.getEventType())) {
            emailService.sendUserDeletedEmail(event.getEmail());
        }
    }
}
