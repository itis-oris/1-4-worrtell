package dev.wortel.meshok.controller;

import dev.wortel.meshok.entity.Item;
import dev.wortel.meshok.service.ItemService;
import dev.wortel.meshok.service.YandexDiskService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemPhotoProxyController {
    private final ItemService itemService;
    private final YandexDiskService yandexDiskService;

    @GetMapping("/{itemId}/photo/{fileName:.+}")
    public void proxyPhoto(
            @PathVariable Long itemId,
            @PathVariable String fileName,
            HttpServletResponse response) {
        Item item = itemService.getItemByMeshokId(itemId);
        System.out.println("Item: " + item);
        if (item == null || item.getMeshokId() == null) {
            response.setStatus(404);
            return;
        }
        String folder = "meshok/" + item.getMeshokId();
        System.out.println("Folder: " + folder);
        log.info("filename: {}", fileName);
        try {
            byte[] imageBytes = yandexDiskService.getPhotoBytes(folder, fileName, itemId);

            String ext = FilenameUtils.getExtension(fileName).toLowerCase();
            String mime = switch (ext) {
                case "jpg", "jpeg" -> MediaType.IMAGE_JPEG_VALUE;
                default -> MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE;
            };
            response.setContentType(mime);
            response.getOutputStream().write(imageBytes);
            response.flushBuffer();
        } catch (Exception e) {
            response.setStatus(404);
        }
    }
}