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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
public class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();

        User user = new User();
        user.setName("Kate");
        user.setEmail("kate@xx.com");
        user.setAge(40);
        userRepository.save(user);
    }

    @Test
    public void getAllUsers() {
        ResponseEntity<List<UserDTO>> response = restTemplate.exchange(
                "/api/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserDTO>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        assertEquals("Kate", response.getBody().get(0).getName());

    }

    @Test
    public void getUserByIdr() {
        ResponseEntity<List<UserDTO>> listResponse = restTemplate.exchange(
                "/api/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserDTO>>() {}
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
        UserRequestDTO newUser = new UserRequestDTO();
        newUser.setName("Test");
        newUser.setEmail("test@test.com");
        newUser.setAge(30);

        ResponseEntity<UserDTO> response = restTemplate.postForEntity(
                "/api/users",
                newUser,
                //String.class
                UserDTO.class
        );


        UserDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Test", responseBody.getName());
        assertEquals("test@test.com", responseBody.getEmail());
        assertNotNull(responseBody.getId());

        /*assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("redirect:/users", response.getBody());

        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
        assertEquals("Test", users.get(0).getName());
        */

    }
}
