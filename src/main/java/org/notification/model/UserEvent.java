package org.notification.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserEvent implements Serializable {
    private String email;
    private EventType eventType;

    public enum EventType {
        CREATED, DELETED
    }

}