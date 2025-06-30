package org.dao;

import org.model.User;
import org.util.HibernateUtil;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDaoIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private static UserDao userDao;

    @BeforeAll
    static void beforeAll() {
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());
        System.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        userDao = new UserDao();
    }

    @AfterAll
    static void afterAll() {
        HibernateUtil.shutdown();
    }

    @Test
    @Order(1)
    void shouldSaveUser() {
        User user = new User(null, 39, "Igor", "test@email.com", LocalDateTime.now());
        User savedUser = userDao.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("Igor", savedUser.getName());
    }

    @Test
    @Order(2)
    void shouldFindUserById() {
        User user = new User(null, 35, "Katya", "find@email.com", LocalDateTime.now());
        User savedUser = userDao.save(user);

        Optional<User> foundUser = userDao.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
    }

    @Test
    @Order(3)
    void shouldUpdateUser() {
        User user = new User(null, 32 , "Andrew", "update@email.com", LocalDateTime.now());
        User savedUser = userDao.save(user);

        savedUser.setName("Andrew");
        User updatedUser = userDao.save(savedUser);

        assertEquals("Andrew", updatedUser.getName());
    }

    @Test
    @Order(4)
    void shouldDeleteUser() {
        User user = new User(null, 30, "Nastya", "delete@email.com", LocalDateTime.now());
        User savedUser = userDao.save(user);

        userDao.delete(savedUser.getId());
        Optional<User> deletedUser = userDao.findById(savedUser.getId());

        assertFalse(deletedUser.isPresent());
    }

    @Test
    @Order(5)
    void shouldFindAllUsers() {
        userDao.save(new User(null, 15, "User 1", "user1@email.com", LocalDateTime.now()));
        userDao.save(new User(null, 17, "User 2", "user2@email.com", LocalDateTime.now()));

        var users = userDao.findAll();

        assertFalse(users.isEmpty());
        assertTrue(users.size() >= 2);
    }
}