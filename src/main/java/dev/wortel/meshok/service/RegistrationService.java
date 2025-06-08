package dev.wortel.meshok.service;

import lombok.RequiredArgsConstructor;
import dev.wortel.meshok.dto.RegistrationForm;
import dev.wortel.meshok.exception.UserAlreadyExistException;
import dev.wortel.meshok.mapper.UserMapper;
import dev.wortel.meshok.repository.UserRepository;
import dev.wortel.meshok.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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

        String encode = passwordEncoder.encode(form.getPassword());
        User user = userMapper.mapToUser(form, encode);
        user = userRepository.save(user);

        confirmService.confirm(user.getEmail(), user.getEmail());
    }

}
