package dev.wortel.meshok.service;

import dev.wortel.meshok.entity.Item;
import dev.wortel.meshok.entity.ItemStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dev.wortel.meshok.helper.PictureHelper;
import dev.wortel.meshok.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static dev.wortel.meshok.entity.ItemStatus.ACTIVE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final YandexDiskService diskService;
    private final PictureHelper pictureHelper;
    private final YandexS3Service s3Service;

    public List<Item> getItemsByIds(List<Long> ids) {
        return itemRepository.findAllByIdInAndItemStatus(ids, ACTIVE);
    }

    public Page<Item> getAllItems(int page, int size) {
        //return itemRepository.findAll(PageRequest.of(page, size));
        return itemRepository.findAllByItemStatus(ACTIVE, PageRequest.of(page, size));
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public Item getItemByMeshokId(Long id) {
        return itemRepository.findByMeshokIdAndItemStatus(id, ACTIVE)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }

    public Page<Item> searchItems(String query, int page, int size) {
        String searchTerm = query.trim();
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        if (searchTerm.isEmpty()) {
            return itemRepository.findAllByItemStatus(ACTIVE, pageable);
        }

        return itemRepository.findByItemStatusAndNameContainingIgnoreCaseOrItemStatusAndDescriptionContainingIgnoreCase(
                ACTIVE,
                searchTerm,
                ACTIVE,
                searchTerm,
                pageable
        );
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

    public Item createItem(Item item, MultipartFile[] images) {
        Item savedItem = itemRepository.save(item);

        if (images != null && images.length > 0) {
            List<String> imageUrls = new ArrayList<>();

            for (int i = 0; i < images.length; i++) {
                MultipartFile image = images[i];
                if (!image.isEmpty()) {
                    String imageUrl = s3Service.uploadToS3(image, savedItem, i);
                    imageUrls.add(imageUrl);
                }
            }
            log.info("Images {}", imageUrls);
            savedItem.setNumberOfPictures(String.valueOf(imageUrls.size()));
            savedItem = itemRepository.save(savedItem);
        }
        return savedItem;
    }

    public void deleteItem(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            Item itemToDelete = item.get();
            itemToDelete.setItemStatus(ItemStatus.DELETED);
            itemRepository.save(itemToDelete);
        }
    }
}