package org.example.kafka;

import org.example.service.dto.UserEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {

    private static final String TOPIC = "user-events";

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserCreatedEvent(String email) {
        UserEvent event = new UserEvent();
        event.setEventType("CREATE");
        event.setEmail(email);
        kafkaTemplate.send(TOPIC, event);
    }

    public void sendUserDeletedEvent(String email) {
        UserEvent event = new UserEvent();
        event.setEventType("DELETE");
        event.setEmail(email);
        kafkaTemplate.send(TOPIC, event);
    }
}