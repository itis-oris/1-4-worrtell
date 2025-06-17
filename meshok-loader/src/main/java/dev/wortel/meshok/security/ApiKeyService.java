package dev.wortel.meshok.security;

import entity.ApiKey;
import entity.User;
import entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    @Transactional
    public Authentication validateApiKey(String apiKeyValue) {
        return apiKeyRepository.findByKeyValueAndActiveTrue(apiKeyValue)
                .filter(key -> !isExpired(key))
                .map(key -> {
                    User user = key.getUser();
                    log.info("user {} key {}", user, key);


                    if (user.getRole() != UserRole.OWNER || !user.isActive()) {
                        log.info("API key belongs to non-admin or inactive user: {}", user.getEmail());
                        return null;
                    }

                    apiKeyRepository.save(key);

                    List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_OWNER")
                    );

                    return new ApiKeyAuthentication(user.getEmail(), apiKeyValue, authorities);
                })
                .orElse(null);
    }

    private boolean isExpired(ApiKey apiKey) {
        return apiKey.getExpiresAt() != null && apiKey.getExpiresAt().isBefore(LocalDateTime.now());
    }
}