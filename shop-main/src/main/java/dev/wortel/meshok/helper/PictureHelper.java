package dev.wortel.meshok.helper;

import entity.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class PictureHelper {
    @Value("${yandex.s3.endpoint}")
    private String S3_ENDPOINT;
    @Value("${yandex.s3.bucket}")
    private String S3_BUCKET;

    public List<String> allCreatedFiles(int count, long id) {
        String baseUrl = S3_ENDPOINT + "/" + S3_BUCKET + "/items." + id + ".";
        log.info(baseUrl);
        return IntStream.range(0, count)
                .mapToObj(i -> baseUrl + i + ".jpg")
                .collect(Collectors.toList());
    }

    public List<String> allFiles(Item item) {
        String id = item.getMeshokId() != null ?  item.getMeshokId().toString() : "items." + item.getId();
        String baseUrl = S3_ENDPOINT + "/" + S3_BUCKET + "/" + id + ".";
        log.info("baseUrl: {}", baseUrl);
        int count = Integer.parseInt(item.getNumberOfPictures());
        return IntStream.range(0, count)
                .mapToObj(i -> baseUrl + i + ".jpg")
                .collect(Collectors.toList());
    }

    public String getFileExtension(String filename) {
        return "jpg";
    }
}