package org.notification;

import org.notification.model.UserEvent;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import jakarta.mail.internet.MimeMessage;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class NotificationServiceApplicationTests {

    @Container
    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.4.0")
    )
            .withExposedPorts(9092, 9093)
            .withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "true")
            .withEnv("KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR", "1")
            .withStartupTimeout(Duration.ofMinutes(2));

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "admin"))
            .withPerMethodLifecycle(false);

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> greenMail.getSmtp().getPort());
    }

    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldSendEmailWhenUserCreatedEventReceived() {
        UserEvent event = new UserEvent();
        event.setEmail("test@example.com");
        event.setEventType(UserEvent.EventType.CREATED);

        kafkaTemplate.send("user-events", event);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
            assertThat(receivedMessages).hasSize(1);
            assertThat(receivedMessages[0].getSubject()).isEqualTo("Добро пожаловать!");
            assertThat(receivedMessages[0].getAllRecipients()[0].toString()).isEqualTo("test@example.com");
        });
    }

    @Test
    void shouldSendEmailViaRestApi() {
        UserEvent event = new UserEvent();
        event.setEmail("rest@example.com");
        event.setEventType(UserEvent.EventType.DELETED);

        restTemplate.postForEntity("/api/notifications/send", event, Void.class);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
            assertThat(receivedMessages).hasSize(1);
            assertThat(receivedMessages[0].getSubject()).isEqualTo("Ваш аккаунт был удалён");
            assertThat(receivedMessages[0].getAllRecipients()[0].toString()).isEqualTo("rest@example.com");
        });
    }
}