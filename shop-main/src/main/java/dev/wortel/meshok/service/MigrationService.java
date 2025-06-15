package dev.wortel.meshok.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MigrationService {

    private final SystemApiKeyService systemApiKeyService;
    private final RestTemplate restTemplate;

    @Value("${migrator.api.url}")
    private String migratorApiUrl;

    public MigrationService(SystemApiKeyService systemApiKeyService) {
        this.systemApiKeyService = systemApiKeyService;
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> triggerMigration() {
        String apiKey = systemApiKeyService.getMigrationServiceApiKey();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    migratorApiUrl + "/api/migrate/start",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            log.info("Migration triggered successfully: {}", response.getBody());

            @SuppressWarnings("unchecked")
            Map<String, Object> result = response.getBody();
            return result != null ? result : Collections.emptyMap();
        } catch (Exception e) {
            log.error("Error triggering migration", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("error", e.getMessage());
            return errorResult;
        }
    }
}
