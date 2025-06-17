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


@Service
@Slf4j
public class SystemApiKeyService {
    private final ApiKeyRepository apiKeyRepository;
    private final UserRepository userRepository;

    @Value("${system.api.key.migration.service}")
    private String migrationServiceName;

    @Value("${system.api.key.validity.days}")
    private int keyValidityDays;

    public SystemApiKeyService(ApiKeyRepository apiKeyRepository, UserRepository userRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.userRepository = userRepository;
    }

    public String ensureSystemApiKey(String serviceName) {
        User systemUser = userRepository.findByEmail("system@internal")
                .orElseGet(() -> {
                    User user = new User();
                    user.setEmail("system@internal");
                    user.setHashPassword(UUID.randomUUID().toString());
                    user.setRole(UserRole.OWNER);
                    return userRepository.save(user);
                });

        Optional<ApiKey> existingKey = apiKeyRepository.findByServiceNameAndActiveTrue(serviceName);

        if (existingKey.isPresent() && existingKey.get().getExpiresAt().isAfter(LocalDateTime.now().plusDays(7))) {
            return existingKey.get().getKeyValue();
        }

        if (existingKey.isPresent()) {
            existingKey.get().setActive(false);
            apiKeyRepository.save(existingKey.get());
        }

        ApiKey newKey = new ApiKey();
        newKey.setKeyValue(generateSecureApiKey());
        newKey.setName("System-" + serviceName);
        newKey.setUser(systemUser);
        newKey.setActive(true);
        newKey.setServiceName(serviceName);
        newKey.setCreatedAt(LocalDateTime.now());
        newKey.setExpiresAt(LocalDateTime.now().plusDays(keyValidityDays));

        ApiKey savedKey = apiKeyRepository.save(newKey);

        log.info("Created new system API key for service: {}, valid until: {}",
                serviceName, savedKey.getExpiresAt());

        return savedKey.getKeyValue();
    }

    private String generateSecureApiKey() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public String getMigrationServiceApiKey() {
        return ensureSystemApiKey(migrationServiceName);
    }
}