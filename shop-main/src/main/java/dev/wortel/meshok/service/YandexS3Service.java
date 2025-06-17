package dev.wortel.meshok.service;

import dev.wortel.meshok.error.BusinessException;
import dev.wortel.meshok.error.ValidationException;
import entity.Item;
import dev.wortel.meshok.helper.PictureHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class YandexS3Service {
    private final S3Client s3Client;
    private final PictureHelper pictureHelper;

    @Value("${yandex.s3.bucket}")
    private String bucketName;

    public String uploadToS3(MultipartFile file, Item item, int index) {
        validateUploadParameters(file, item);

        try {
            String filename = generateFilename(item, index,
                    pictureHelper.getFileExtension(file.getOriginalFilename()));

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

            return buildFileUrl(filename);

        } catch (IOException e) {
            log.error("Error uploading file to S3", e);
            throw new BusinessException("Failed to upload file to S3");
        }
    }

    private String generateFilename(Item item, int index, String extension) {
        return "items/" + item.getId() + "_" + index + "." + extension;
    }

    private String buildFileUrl(String filename) {
        return "https://storage.yandexcloud.net/" + bucketName + "/" + filename;
    }

    private void validateUploadParameters(MultipartFile file, Item item) {
        if (file == null || file.isEmpty()) {
            throw new ValidationException("File cannot be empty");
        }
        if (item == null || item.getId() == null) {
            throw new ValidationException("Item is not valid");
        }
    }
}