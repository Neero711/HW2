package org.example.notificationservice.dto;

import lombok.Data;

@Data
public class UserEvent {
    private String eventType;
    private String email;
    private Long userId;

    public String getEventType() {
        return eventType;
    }

    public String getEmail() {
        return email;
    }

    public Long getUserId() {
        return userId;
    }
}