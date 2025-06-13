package dev.wortel.meshok.service;

import dev.wortel.meshok.entity.Item;
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
    @Value("${yandex.s3.bucket}")
    private String bucketName;
    private final PictureHelper pictureHelper;

    public String fetchAndUploadToS3(Item item, int n){
        try {
            String imageUrl = pictureHelper.path(item, n);
            log.info("Fetching image from {}", imageUrl);
            URL url = new URL(imageUrl);

            String filename = pictureHelper.filename(item, n);
            log.info("Uploading image to {}", filename);

            try (InputStream in = url.openStream()) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                in.transferTo(buffer);
                byte[] bytes = buffer.toByteArray();

                PutObjectRequest request = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(filename)
                        .contentType("image/jpeg")
                        .build();

                s3Client.putObject(request, RequestBody.fromBytes(bytes));
            }

            return "https://storage.yandexcloud.net/" + bucketName + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String uploadToS3(MultipartFile file, Item item, int index) {
        try {
            String filename = generateFilename(item, index, pictureHelper.getFileExtension(file.getOriginalFilename()));
            log.info("Uploading image to S3: {}", filename);

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request,
                    RequestBody.fromBytes(file.getBytes()));

            return "https://storage.yandexcloud.net/" + bucketName + "/" + filename;
        } catch (IOException e) {
            log.error("Error uploading file to S3", e);
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    private String generateFilename(Item item, int index, String extension) {
        return "items." + item.getId() + "." + index + "." + extension;
    }
}