package dev.wortel.meshok.helper;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PictureHelper {
    @Value("${yandex.disk.public-url}")
    private String PUBLIC_URL;

    @Value("${yandex.disk.base-url}")
    private String BASE_URL="https://cloud-api.yandex.net/v1/disk";

    @Value("${meshok.base-url}")
    private String MESHOK_URL="https://meshok.net/i/";

    public String path(String id, Integer n) {
        return MESHOK_URL + "/" + id + "." + n + ".jpg?1";
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
}
