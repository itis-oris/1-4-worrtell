package dev.wortel.meshok.service;

import dev.wortel.meshok.entity.User;
import dev.wortel.meshok.exception.ResourceNotFoundException;
import dev.wortel.meshok.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email " + email));
    }
}
