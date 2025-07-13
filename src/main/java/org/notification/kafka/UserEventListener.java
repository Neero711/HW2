package org.notification.kafka;


import org.notification.model.UserEvent;
import org.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final EmailService emailService;

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void handleUserEvent(UserEvent event) {
        switch (event.getEventType()) {
            case CREATED:
                emailService.sendAccountCreatedEmail(event.getEmail());
                break;
            case DELETED:
                emailService.sendAccountDeletedEmail(event.getEmail());
                break;
        }
    }
}