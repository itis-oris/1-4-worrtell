package dev.wortel.meshok.event;

import dev.wortel.meshok.entity.User;
import dev.wortel.meshok.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.repository.dictonary.UserRole;
import org.example.repository.dictonary.UserStatus;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationReadyEventListener {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "anvar000755@mail.ru";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Application ready");
        if (userRepository.findByEmail(ADMIN_USERNAME).isEmpty()) {
            User user = new User()
                    .setEmail(ADMIN_EMAIL)
                    .setHashPassword(passwordEncoder.encode("123"))
                    .setRole(UserRole.ADMIN)
                    .setStatus(UserStatus.ACTIVE);

            userRepository.save(user);
        }
    }
}
