package dev.wortel.meshok.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;

import java.io.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class YandexDiskExcelService {
    private static final String BASE_URL = "https://cloud-api.yandex.net/v1/disk";
    private final RestClient restClient = RestClient.builder()
            .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                throw new RuntimeException("API error: " + response.getStatusCode() + " - " + response.getStatusText());
            })
            .build();

    @Value("${yandex.disk.access-token}")
    private String accessToken;

    public void updateExcelFile(String remotePath, Map<String, String> newData) throws IOException {
        validatePath(remotePath);

        try {
            // 1. Проверяем существование файла
            checkFileExists(remotePath);

            // 2. Скачиваем файл
            byte[] fileContent = downloadFile(remotePath);

            // 3. Обновляем Excel
            byte[] updatedContent = updateExcel(fileContent, newData);

            // 4. Загружаем обратно
            uploadFile(remotePath, updatedContent);

        } catch (Exception e) {
            throw new IOException("Ошибка при обновлении файла: " + e.getMessage(), e);
        }
    }

    private void checkFileExists(String path) {
        try {
            restClient.get()
                    .uri(BASE_URL + "/resources?path=" + encode(path))
                    .header("Authorization", "OAuth " + accessToken)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            throw new RuntimeException("Файл не существует или нет доступа: " + path, e);
        }
    }

    private byte[] downloadFile(String path) throws IOException {
        try {
            // 1. Получаем временную ссылку для скачивания
            String downloadUrl = getDownloadUrl(path);

            if (downloadUrl == null || downloadUrl.isEmpty()) {
                throw new IOException("Failed to get valid download URL");
            }

            // 2. Создаем отдельный RestClient без базового URL и обработчиков ошибок
            RestClient simpleClient = RestClient.create();

            // 3. Скачиваем файл (без заголовка Authorization!)
            byte[] fileContent = simpleClient.get()
                    .uri(downloadUrl)
                    .accept(MediaType.APPLICATION_OCTET_STREAM)
                    .retrieve()
                    .body(byte[].class);

            // 4. Проверяем, что файл не пустой
            if (fileContent == null || fileContent.length == 0) {
                throw new IOException("Downloaded file is empty");
            }

            // 5. Быстрая проверка, что это Excel файл
            if (!(fileContent[0] == 0x50 && fileContent[1] == 0x4B)) { // PK header
                throw new IOException("Downloaded file is not a valid Excel document");
            }

            return fileContent;

        } catch (RestClientException e) {
            throw new IOException("Failed to download file from Yandex.Disk: " + e.getMessage(), e);
        }
    }

    // Вспомогательный метод для проверки формата файла
    private boolean isValidExcelFile(byte[] content) {
        try (InputStream is = new ByteArrayInputStream(content)) {
            // Проверяем сигнатуру Excel файла (первые 4 байта)
            byte[] signature = new byte[4];
            is.read(signature);
            return Arrays.equals(signature, new byte[]{0x50, 0x4B, 0x03, 0x04}); // PK..
        } catch (Exception e) {
            return false;
        }
    }

    private String getDownloadUrl(String path) {
        try {
            return restClient.get()
                    .uri(BASE_URL + "/resources/download?path=" + encode(path))
                    .header("Authorization", "OAuth " + accessToken)
                    .retrieve()
                    .body(Map.class)
                    .get("href")
                    .toString();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось получить ссылку для скачивания", e);
        }
    }

    private byte[] updateExcel(byte[] fileContent, Map<String, String> newData) throws IOException {
        try (InputStream is = new ByteArrayInputStream(fileContent);
             Workbook workbook = WorkbookFactory.create(is);
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.getSheetAt(0);
            int lastRow = sheet.getLastRowNum() == -1 ? 0 : sheet.getLastRowNum() + 1;

            // Добавляем новые данные
            for (Map.Entry<String, String> entry : newData.entrySet()) {
                Row row = sheet.createRow(lastRow++);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue());
            }

            workbook.write(os);
            return os.toByteArray();
        } catch (Exception e) {
            throw new IOException("Ошибка при обработке Excel: " + e.getMessage(), e);
        }
    }

    private void uploadFile(String path, byte[] content) {
        try {
            String uploadUrl = restClient.get()
                    .uri(BASE_URL + "/resources/upload?path=" + encode(path) + "&overwrite=true")
                    .header("Authorization", "OAuth " + accessToken)
                    .retrieve()
                    .body(Map.class)
                    .get("href")
                    .toString();

            restClient.put()
                    .uri(uploadUrl)
                    .header("Authorization", "OAuth " + accessToken)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(content)
                    .retrieve()
                    .toBodilessEntity();

        } catch (Exception e) {
            throw new RuntimeException("Не удалось загрузить файл: " + e.getMessage(), e);
        }
    }

    private void validatePath(String path) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Путь к файлу не может быть пустым");
        }
        if (!path.toLowerCase().endsWith(".xlsx")) {
            throw new IllegalArgumentException("Поддерживаются только файлы .xlsx");
        }
    }

    private String encode(String path) {
        return URLEncoder.encode(path, StandardCharsets.UTF_8)
                .replace("+", "%20")
                .replace("%2F", "/");
    }
}