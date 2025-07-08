package org.example.controller;

import org.example.dto.UserDTO;
import org.example.dto.UserRequestDTO;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
public class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private UserDTO testUser;


    @BeforeEach
    public void setup() {
        userRepository.deleteAll();

        UserRequestDTO request = new UserRequestDTO();
        request.setName("Test");
        request.setEmail("test@test.com");
        request.setAge(27);

        ResponseEntity<UserDTO> response = restTemplate.postForEntity(
                "/api/users",
                request,
                UserDTO.class
        );
        testUser = response.getBody();
    }


    @Test
    public void getAllUsers() {
        ResponseEntity<List> response = restTemplate.getForEntity(
                "/api/users",
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());

    }

    @Test
    public void getUserById() {
        ResponseEntity<List<UserDTO>> listResponse = restTemplate.exchange(
                "/api/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserDTO>>() {
                }
        );
        Long userId = listResponse.getBody().get(0).getId();

        ResponseEntity<UserDTO> response = restTemplate.getForEntity(
                "/api/users/" + userId,
                UserDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userId, response.getBody().getId());
    }

    @Test
    public void createUser() {
        userRepository.deleteAll();
        UserRequestDTO request = new UserRequestDTO();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setAge(25);

        restTemplate.postForEntity("/api/users", request, Void.class);

        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());

        User savedUser = users.get(0);
        assertEquals("Test User", savedUser.getName());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals(25, savedUser.getAge());
    }

    @Test
    public void updateUser() {
        UserRequestDTO updateRequest = new UserRequestDTO();
        updateRequest.setName("updName");
        updateRequest.setEmail("updated@upd.com");
        updateRequest.setAge(35);

        HttpEntity<UserRequestDTO> requestEntity = new HttpEntity<>(updateRequest);
        ResponseEntity<UserDTO> response = restTemplate.exchange(
                "/api/users/" + testUser.getId(),
                HttpMethod.PUT,
                requestEntity,
                UserDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("updName", response.getBody().getName());
        assertEquals("updated@upd.com", response.getBody().getEmail());
    }

    @Test
    public void deleteUser() {
        // Создаем пользователя
        User user = new User();
        user.setName("obj1");
        user.setEmail("obj@obj.com");
        user.setAge(20);
        User savedUser = userRepository.save(user);

        UserRequestDTO updateRequest = new UserRequestDTO();
        updateRequest.setName("obj2");
        updateRequest.setEmail("obj2@obj.com");
        updateRequest.setAge(23);

        restTemplate.put("/api/users/" + savedUser.getId(), updateRequest);

        User updatedUser = userRepository.findById(savedUser.getId()).orElseThrow();
        assertEquals("obj2", updatedUser.getName());
        assertEquals("obj2@obj.com", updatedUser.getEmail());
        assertEquals(23, updatedUser.getAge());
    }
}

