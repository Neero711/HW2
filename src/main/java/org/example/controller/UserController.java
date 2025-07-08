package org.example.controller;

import ch.qos.logback.core.model.Model;
import org.example.dto.UserDTO;
import org.example.dto.UserRequestDTO;
import org.example.service.UserService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.*;


import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRequestDTO user) {
        UserDTO createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}