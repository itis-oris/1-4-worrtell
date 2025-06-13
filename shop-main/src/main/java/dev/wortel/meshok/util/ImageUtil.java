package dev.wortel.meshok.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Slf4j
@Component
public class ImageUtil {
    @Value("${yandex.disk.public-url}")
    private static String BASE_URL;

    public static String path(String id, Integer n) {
        return BASE_URL + id + "." + n + ".jpg";
    }
}