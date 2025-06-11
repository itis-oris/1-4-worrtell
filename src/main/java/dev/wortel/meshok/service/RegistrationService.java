package dev.wortel.meshok.service;

import lombok.RequiredArgsConstructor;
import dev.wortel.meshok.dto.RegistrationForm;
import dev.wortel.meshok.exception.UserAlreadyExistException;
import dev.wortel.meshok.mapper.UserMapper;
import dev.wortel.meshok.repository.UserRepository;
import dev.wortel.meshok.entity.User;
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
    private final ConfirmService confirmService;


    public void createUser(RegistrationForm form) {
        userRepository.findByEmail(form.getEmail())
                .ifPresent(user -> {
                    throw new UserAlreadyExistException("user exist");
                });
        log.info("Creating user {} with password {}", form.getEmail(), form.getPassword());
        String encode = passwordEncoder.encode(form.getPassword());
        User user = userMapper.mapToUser(form, encode);
        user = userRepository.save(user);
        log.info("User {} created with password {}", user.getEmail(), encode);
        confirmService.confirm(user.getEmail(), user.getEmail());
    }
}