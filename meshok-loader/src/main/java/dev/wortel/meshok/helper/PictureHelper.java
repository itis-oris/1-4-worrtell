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
    @Value("${meshok.base-url}")
    private String MESHOK_URL;

    public String path(String id, Integer n) {
        return MESHOK_URL + "/" + id + "." + n + ".jpg?1";
    }

    public String path(Item item, int n) {
        return MESHOK_URL + item.getMeshokId() + "." + n + ".jpg?1";
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
}
