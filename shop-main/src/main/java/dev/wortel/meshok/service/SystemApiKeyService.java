package dev.wortel.meshok.service;
import dev.wortel.meshok.error.BusinessException;
import dev.wortel.meshok.error.ValidationException;
import dev.wortel.meshok.repository.UserRepository;
import dev.wortel.meshok.repository.ApiKeyRepository;
import entity.ApiKey;
import entity.User;
import entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SystemApiKeyService {
    private final ApiKeyRepository apiKeyRepository;
    private final UserRepository userRepository;

    @Value("${system.api.key.migration.service}")
    private String migrationServiceName;

    @Value("${system.api.key.validity.days}")
    private int keyValidityDays;

    public String getMigrationServiceApiKey() {
        return ensureSystemApiKey(migrationServiceName);
    }

    public String ensureSystemApiKey(String serviceName) {
        validateServiceName(serviceName);

        try {
            User systemUser = getOrCreateSystemUser();
            Optional<ApiKey> existingKey = findValidApiKey(serviceName);

            if (existingKey.isPresent()) {
                return existingKey.get().getKeyValue();
            }

            return createNewApiKey(serviceName, systemUser).getKeyValue();

        } catch (Exception e) {
            log.error("Failed to manage system API key", e);
            throw new BusinessException("Failed to manage system API keys");
        }
    }

    private User getOrCreateSystemUser() {
        return userRepository.findByEmail("system@internal")
                .orElseGet(() -> {
                    User user = new User();
                    user.setEmail("system@internal");
                    user.setHashPassword(UUID.randomUUID().toString());
                    user.setRole(UserRole.ADMIN);
                    return userRepository.save(user);
                });
    }

    private Optional<ApiKey> findValidApiKey(String serviceName) {
        Optional<ApiKey> existingKey = apiKeyRepository
                .findByServiceNameAndActiveTrue(serviceName);

        if (existingKey.isPresent()) {
            if (existingKey.get().getExpiresAt().isAfter(LocalDateTime.now().plusDays(7))) {
                return existingKey;
            }
            deactivateKey(existingKey.get());
        }

        return Optional.empty();
    }

    private void deactivateKey(ApiKey key) {
        key.setActive(false);
        apiKeyRepository.save(key);
    }

    private ApiKey createNewApiKey(String serviceName, User systemUser) {
        ApiKey newKey = new ApiKey();
        newKey.setKeyValue(generateSecureApiKey());
        newKey.setName("System-" + serviceName);
        newKey.setUser(systemUser);
        newKey.setActive(true);
        newKey.setServiceName(serviceName);
        newKey.setCreatedAt(LocalDateTime.now());
        newKey.setExpiresAt(LocalDateTime.now().plusDays(keyValidityDays));

        return apiKeyRepository.save(newKey);
    }

    private String generateSecureApiKey() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private void validateServiceName(String serviceName) {
        if (serviceName == null || serviceName.isBlank()) {
            throw new ValidationException("Service name is required");
        }
    }
}