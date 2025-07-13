package org.notification;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class NotificationServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(NotificationServiceApplication.class)
                .properties("server.port=8081")
                .run(args);
    }
}
