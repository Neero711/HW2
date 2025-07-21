package org.example.service.dto;

import lombok.Data;

@Data
public class UserEvent {
    private String eventType;
    private String email;
    private Long userId;
}
