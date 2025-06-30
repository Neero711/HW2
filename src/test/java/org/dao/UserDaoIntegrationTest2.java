package org.dao;

import org.model.User;
import org.util.HibernateUtil;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDaoIntegrationTest2 {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private static UserDao userDao;
    private static int userCounter = 0;

    @BeforeAll
    static void setup() {
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());
        System.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        userDao = new UserDao();
    }

    private User createUniqueUser() {
        userCounter++;
        return new User(
                null,
                20 + userCounter,
                "User " + userCounter,
                "user" + userCounter + "@test.com",
                LocalDateTime.now()
        );
    }

    @Test
    @Order(1)
    void shouldSaveUser() {
        User user = createUniqueUser();
        User savedUser = userDao.save(user);
        assertNotNull(savedUser.getId());
    }

    @Test
    @Order(2)
    void shouldFindUserById() {
        User user = createUniqueUser();
        User savedUser = userDao.save(user);

        Optional<User> found = userDao.findById(savedUser.getId());
        assertTrue(found.isPresent());
        assertEquals(savedUser.getId(), found.get().getId());
    }

    @Test
    @Order(3)
    void shouldUpdateUser() {
        User user = createUniqueUser();
        User savedUser = userDao.save(user);

        savedUser.setName("Updated Name");
        User updated = userDao.save(savedUser);

        assertEquals("Updated Name", updated.getName());
    }

    @Test
    @Order(4)
    void shouldDeleteUser() {
        User user = createUniqueUser();
        User savedUser = userDao.save(user);

        userDao.delete(savedUser.getId());
        Optional<User> deleted = userDao.findById(savedUser.getId());

        assertFalse(deleted.isPresent());
    }

    @Test
    @Order(5)
    void shouldFindAllUsers() {
        userDao.save(createUniqueUser());
        userDao.save(createUniqueUser());

        List<User> users = userDao.findAll();
        assertTrue(users.size() >= 2);
    }

    @AfterAll
    static void cleanup() {
        HibernateUtil.shutdown();
    }
}