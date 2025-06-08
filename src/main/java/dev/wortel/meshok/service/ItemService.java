package dev.wortel.meshok.service;

import dev.wortel.meshok.entity.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dev.wortel.meshok.helper.PictureHelper;
import dev.wortel.meshok.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final YandexDiskService diskService;
    private final PictureHelper pictureHelper;

    public Page<Item> getAllItems(int page, int size) {
        return itemRepository.findAll(PageRequest.of(page, size));
    }

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
            log.error("Failed to save item {}: {}", item.getMeshok_id(), e.getMessage());
        }
    }

    private void savePictures(Item item) {
        int n = Integer.parseInt(item.getNumberOfPictures());
        String id = String.valueOf(item.getMeshok_id());

        for (int i = 0; i < n; i++) {
            String url = diskService.uploadFile(
                    pictureHelper.path(id, i),
                    pictureHelper.newFolder(id),
                    pictureHelper.name(i)
            );
            if (url == null) {
                log.warn("Picture {} for item {} not uploaded", i, id);
            }
        }
    }
}
