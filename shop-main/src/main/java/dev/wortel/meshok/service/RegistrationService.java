package dev.wortel.meshok.service;

import dev.wortel.meshok.error.BusinessException;
import dev.wortel.meshok.error.ValidationException;
import lombok.RequiredArgsConstructor;
import dev.wortel.meshok.dto.RegistrationForm;
import dev.wortel.meshok.exception.UserAlreadyExistException;
import dev.wortel.meshok.mapper.UserMapper;
import dev.wortel.meshok.repository.UserRepository;
import entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void createUser(RegistrationForm form) {
        validateRegistrationForm(form);

        try {
            userRepository.findByEmail(form.getEmail())
                    .ifPresent(user -> {
                        throw new BusinessException("User with this email already exists");
                    });

            String encodedPassword = passwordEncoder.encode(form.getPassword());
            User user = userMapper.mapToUser(form, encodedPassword);
            userRepository.save(user);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to create user", e);
            throw new BusinessException("Failed to register user");
        }
    }

    private void validateRegistrationForm(RegistrationForm form) {
        if (form == null) {
            throw new ValidationException("Registration data is required");
        }
        if (form.getEmail() == null || form.getEmail().isBlank()) {
            throw new ValidationException("Email is required");
        }
        if (form.getPassword() == null || form.getPassword().isBlank()) {
            throw new ValidationException("Password is required");
        }
    }
}