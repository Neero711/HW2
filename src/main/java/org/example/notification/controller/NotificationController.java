package org.example.notification.controller;

import org.example.notification.model.UserEvent;
import org.example.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/send")
    public void sendNotification(@RequestBody UserEvent event) {
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