package org.service;

import org.dao.UserDao;
import org.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, 27, "Test User", "test@email.com", LocalDateTime.now());
    }

    @Test
    void shouldCreateUser() {
        when(userDao.save(any(User.class))).thenReturn(testUser);

        User created = userService.createUser(testUser);

        assertNotNull(created);
        assertEquals("Test User", created.getName());
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void shouldFindUserById() {
        when(userDao.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> found = userService.getUserById(1L);

        assertTrue(found.isPresent());
        assertEquals("test@email.com", found.get().getEmail());
        verify(userDao, times(1)).findById(1L);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        when(userDao.findById(999L)).thenReturn(Optional.empty());

        Optional<User> found = userService.getUserById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void shouldUpdateUser() {
        when(userDao.findById(1L)).thenReturn(Optional.of(testUser));
        when(userDao.save(any(User.class))).thenReturn(testUser);

        testUser.setName("Updated Name");
        User updated = userService.updateUser(testUser);

        assertEquals("Updated Name", updated.getName());
        verify(userDao, times(1)).findById(1L);
        verify(userDao, times(1)).save(testUser);
    }

    @Test
    void shouldThrowWhenUpdateNonExistingUser() {
        when(userDao.findById(999L)).thenReturn(Optional.empty());

        User nonExisting = new User(999L, 88, "None user", "none@email.com", LocalDateTime.now());

        assertThrows(RuntimeException.class, () -> userService.updateUser(nonExisting));
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    void shouldDeleteUser() {
        when(userDao.findById(1L)).thenReturn(Optional.of(testUser));
        doNothing().when(userDao).delete(1L);

        userService.deleteUser(1L);

        verify(userDao, times(1)).delete(1L);
    }

    @Test
    void shouldFindAllUsers() {
        List<User> users = List.of(
                new User(1L, 15, "user1", "user1@email.com", LocalDateTime.now()),
                new User(2L, 17, "user2", "user2@email.com", LocalDateTime.now())
        );

        when(userDao.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userDao, times(1)).findAll();
    }
}