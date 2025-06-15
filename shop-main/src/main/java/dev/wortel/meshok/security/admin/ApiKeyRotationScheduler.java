package dev.wortel.meshok.security.admin;

import dev.wortel.meshok.service.SystemApiKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApiKeyRotationScheduler {

    private final SystemApiKeyService systemApiKeyService;

    public ApiKeyRotationScheduler(SystemApiKeyService systemApiKeyService) {
        this.systemApiKeyService = systemApiKeyService;
    }

    @Scheduled(cron = "0 0 0 * * SUN")
    public void rotateSystemApiKeys() {
        log.info("Starting scheduled rotation of system API keys");

        try {
            systemApiKeyService.getMigrationServiceApiKey();

            log.info("System API key rotation completed successfully");
        } catch (Exception e) {
            log.error("Error during system API key rotation", e);
        }
    }
}