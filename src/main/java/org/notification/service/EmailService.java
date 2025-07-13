package org.notification.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.notification.site-url}")
    private String siteUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAccountCreatedEmail(String toEmail) {
        String subject = "Добро пожаловать!";
        String text = String.format("Здравствуйте! Ваш аккаунт на сайте %s был успешно создан.", siteUrl);

        sendEmail(toEmail, subject, text);
    }

    public void sendAccountDeletedEmail(String toEmail) {
        String subject = "Ваш аккаунт был удалён";
        String text = "Здравствуйте! Ваш аккаунт был удалён.";

        sendEmail(toEmail, subject, text);
    }

    private void sendEmail(String toEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}