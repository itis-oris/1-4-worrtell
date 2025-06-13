package dev.wortel.meshok.event;

import entity.User;
import entity.UserRole;
import entity.UserStatus;
import dev.wortel.meshok.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationReadyEventListener {
    private static final String ADMIN_EMAIL = "owner@mail.ru";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Application ready");
        if (userRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
            User user = new User()
                    .setEmail(ADMIN_EMAIL)
                    .setHashPassword(passwordEncoder.encode("1"))
                    .setRole(UserRole.OWNER)
                    .setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }
    }
}
