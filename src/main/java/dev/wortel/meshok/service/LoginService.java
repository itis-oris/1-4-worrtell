package dev.wortel.meshok.service;

import dev.wortel.meshok.dto.LoginForm;
import dev.wortel.meshok.dto.RegistrationForm;
import dev.wortel.meshok.entity.User;
import dev.wortel.meshok.exception.UserAlreadyExistException;
import dev.wortel.meshok.mapper.UserMapper;
import dev.wortel.meshok.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class LoginService {
//
//    private final UserRepository userRepository;
//    private final UserMapper userMapper;
//    private final PasswordEncoder passwordEncoder;
//    private final ConfirmService confirmService;
//
//
//    public boolean login(LoginForm form) {
//        log.info("Login form: {}", form);
//        return userRepository.findByEmail(form.getEmail())
//                .map(user -> passwordEncoder.matches(form.getPassword(), user.getHashPassword()))
//                .orElse(false);
//    }
//}
