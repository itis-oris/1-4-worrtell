package dev.wortel.meshok;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        boolean exists = false;
        try {
            exists = imageExists("meshok/325179466/", "1.jpg", "y0__xCG96bWAxjuwzcg_Z7FghNN0TNgJYEPgT9Tb38nxzKYp-DGjw");
            System.out.println("Изображение существует? " + exists);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean imageExists(String folderPath, String fileName, String accessToken) throws Exception {
        Sardine sardine = SardineFactory.begin(accessToken, "");
        String safePath = folderPath.startsWith("/") ? folderPath.substring(1) : folderPath;
        if (!safePath.endsWith("/")) safePath += "/";
        String url = "https://webdav.yandex.ru/" + safePath + fileName;
        return sardine.exists(url);
    }
}