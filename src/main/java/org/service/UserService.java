package org.service;


import org.model.User;
import org.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;



}
