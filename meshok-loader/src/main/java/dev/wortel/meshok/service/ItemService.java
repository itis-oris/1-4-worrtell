package dev.wortel.meshok.service;

import dev.wortel.meshok.repository.ItemRepository;
import entity.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final YandexS3Service s3Service;

    public List<Long> filterNonExistingIds(List<Long> inputIds) {
        List<Long> existingIds = filterExistingIds(inputIds);
        return inputIds.stream()
                .filter(id -> !existingIds.contains(id))
                .toList();
    }

    public List<Long> filterExistingIds(List<Long> inputIds) {
        if (inputIds == null || inputIds.isEmpty()) {
            return Collections.emptyList();
        }
        return itemRepository.findExistingIds(inputIds);
    }

    public void saveWithPictures(List<Item> items) {
        items.forEach(this::saveWithPictures);
    }

    public void saveWithPictures(Item item) {
        try {
            savePictures(item);
            itemRepository.save(item);
        } catch (Exception e) {
            log.error("Failed to save item {}: {}", item.getMeshokId(), e.getMessage());
        }
    }

    private void savePictures(Item item) {
        int n = Integer.parseInt(item.getNumberOfPictures());
//        String id = String.valueOf(item.getMeshokId());

        for (int i = 0; i < n; i++) {
//            String url = diskService.uploadFile(
//                    pictureHelper.path(id, i),
//                    pictureHelper.newFolder(id),
//                    pictureHelper.name(i)
//            );
            s3Service.fetchAndUploadToS3(item, i);
        }
    }
}