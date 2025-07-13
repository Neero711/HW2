package org.example.notification.model;

import lombok.Data;

@Data
public class UserEvent {
    private String email;
    private EventType eventType;

    public enum EventType {
        CREATED, DELETED
    }
}