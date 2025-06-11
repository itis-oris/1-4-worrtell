package dev.wortel.meshok.service;

import com.github.sardine.SardineFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
public class YandexDiskService {
    @Value("${yandex.disk.base-url}")
    private String BASE_URL;
    @Value("${yandex.disk.webdav-url}")
    private String WEBDAV_URL;
    @Value("${yandex.disk.access-token}")
    private String accessToken;
    private final RestClient restClient;

    public YandexDiskService() {
        this.restClient = RestClient.builder()
                .defaultHeader("Authorization", "OAuth " + accessToken)
                .build();
    }

    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000),
            retryFor = { IOException.class, RestClientException.class }
    )
    public String uploadFile(String imageUrl, String folderPath, String filename) {
        String fullPath = folderPath + "/" + filename;
        log.info("Uploading file: {}", fullPath);

        createFolder(folderPath);
        uploadFromUrl(imageUrl, fullPath);
        return makePublic(fullPath);
    }

    @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 1000))
    private void createFolder(String path) {
        try {
            restClient.put()
                    .uri(BASE_URL + "/resources?path=" + path)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.Conflict e) {
            log.info("Folder already exists: {}", path);
        }
    }

    private void uploadFromUrl(String fileUrl, String savePath) {
        try {
            String uploadUrl = getUploadUrl(savePath);
            doUpload(fileUrl, uploadUrl);
        } catch (Exception e) {
            log.error("Failed to upload file from URL: {}", fileUrl, e);
            throw new RuntimeException(e);
        }
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    private String getUploadUrl(String savePath) throws IOException {
        return restClient.get()
                .uri(BASE_URL + "/resources/upload?path=" + savePath + "&overwrite=true")
                .retrieve()
                .body(Map.class)
                .get("href")
                .toString();
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    private void doUpload(String fileUrl, String uploadUrl) throws IOException {
        try (InputStream fileStream = new URL(fileUrl).openStream()) {
            restClient.put()
                    .uri(uploadUrl)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(fileStream))
                    .retrieve()
                    .toBodilessEntity();
        }
    }

    @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 1000))
    private String makePublic(String path) {
        restClient.put()
                .uri(BASE_URL + "/resources/publish?path=" + path)
                .retrieve()
                .toBodilessEntity();

        Map<?, ?> resource = restClient.get()
                .uri(BASE_URL + "/resources?path=" + path)
                .retrieve()
                .body(Map.class);

        return (resource != null && resource.containsKey("public_url"))
                ? resource.get("public_url").toString()
                : "https://disk.yandex.ru/client/disk" + path;
    }

    @Recover
    private String recoverUpload(Exception e, String imageUrl, String folderPath, String filename) {
        log.error("All retry attempts failed for file: {}/{}. Error: {}", folderPath, filename, e.getMessage());
        return null;
    }

    @Cacheable(value = "photoCache", key = "#lotId + ':' + #fileName")
    public byte[] getPhotoBytes(String folderPath, String fileName, Long lotId) throws Exception {
        log.info("Загрузка изображения с Яндекс.Диск: lotId={}, fileName={}", lotId, fileName);
        log.info("Token {}", accessToken);
        String safePath = folderPath.startsWith("/") ? folderPath.substring(1) : folderPath;
        if (!safePath.endsWith("/")) safePath += "/";
        log.info("WEBDAV_URL + safePath + fileName {} {} {}", WEBDAV_URL, safePath, fileName);
        String url = WEBDAV_URL + safePath + fileName;

        log.info("Попытка скачать файл с URL: {}", url);

        try (var input = SardineFactory.begin(accessToken, "").get(url)) {
            byte[] bytes = input.readAllBytes();
            log.info("Успешно скачано {} байт для {}:{}", bytes.length, lotId, fileName);
            return bytes;
        } catch (Exception e) {
            log.info("Ошибка при попытке скачать изображение {}:{} с Диска — {}", lotId, fileName, e.getMessage());
            throw e;
        }
    }
}