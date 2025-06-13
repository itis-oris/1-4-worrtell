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
    @Value("${yandex.disk.public-url}")
    private String PUBLIC_URL;
    @Value("${yandex.disk.base-url}")
    private String BASE_URL;
    @Value("${meshok.base-url}")
    private String MESHOK_URL;
    @Value("${yandex.s3.endpoint}")
    private String S3_ENDPOINT;
    @Value("${yandex.s3.bucket}")
    private String S3_BUCKET;

    public String path(String id, Integer n) {
        return MESHOK_URL + "/" + id + "." + n + ".jpg?1";
    }

    public String path(Item item, int n) {
        return MESHOK_URL + item.getMeshokId() + "." + n + ".jpg?1";
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

    public List<String> allCreatedFiles(int count, long id) {
        String baseUrl = S3_ENDPOINT + "/" + S3_BUCKET + "/items." + id + ".";
        log.info(baseUrl);
        return IntStream.range(0, count)
                .mapToObj(i -> baseUrl + i + ".jpg")
                .collect(Collectors.toList());
    }

    public String filename(Item item, int n) {
        return item.getMeshokId() + "." + n + ".jpg";
    }

    public String folder(Long id) {
        return PUBLIC_URL + "/" + id;
    }

    public String name(Integer n) {
        return n + ".jpg";
    }

    public String newFolder(String id) {
        return "/meshok/" + id;
    }

    public String getFileExtension(String filename) {
//        if (filename == null) return "jpg";
//        int lastDotIndex = filename.lastIndexOf('.');
//        if (lastDotIndex == -1) return "jpg";
//        return filename.substring(lastDotIndex + 1);
        return "jpg";
    }
}
