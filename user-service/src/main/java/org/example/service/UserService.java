package org.example.service;

import lombok.*;
import org.example.dto.UserDTO;
import org.example.dto.UserRequestDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.kafka.UserEventProducer;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEventProducer userEventProducer;

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public UserDTO createUser(UserRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        var user = userMapper.toEntity(request);
        userEventProducer.sendUserCreatedEvent(user.getEmail());
        return userMapper.toDTO(userRepository.save(user));
    }

    @Transactional
    public UserDTO updateUser(Long id, UserRequestDTO request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(request.getName());
                    user.setEmail(request.getEmail());
                    user.setAge(request.getAge());
                    return userMapper.toDTO(userRepository.save(user));
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        UserDTO userToDelete = getUserById(id);
        userEventProducer.sendUserDeletedEvent(userToDelete.getEmail());
        userRepository.deleteById(id);
    }

}
