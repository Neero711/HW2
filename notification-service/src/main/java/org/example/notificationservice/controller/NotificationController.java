package org.example.notificationservice.controller;

import org.example.notificationservice.service.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendEmail(
            @RequestParam String email,
            @RequestParam String subject,
            @RequestParam String text) {
        emailService.sendCustomEmail(email, subject, text);
        return "Email sent successfully";
    }
}
