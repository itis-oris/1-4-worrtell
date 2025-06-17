package dev.wortel.meshok.service;

import dev.wortel.meshok.dto.ItemResponse;
import dev.wortel.meshok.dto.ItemUpdateDto;
import dev.wortel.meshok.error.BusinessException;
import dev.wortel.meshok.error.ResourceNotFoundException;
import dev.wortel.meshok.error.ValidationException;
import dev.wortel.meshok.mapper.ItemMapper;
import entity.Item;
import entity.ItemStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dev.wortel.meshok.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static entity.ItemStatus.ACTIVE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final YandexS3Service s3Service;
    private final ItemMapper itemMapper;

    public void changeStatus(Item item, ItemStatus status) {
        validateItemOperation(item, status);

        try {
            item.setItemStatus(status);
            itemRepository.save(item);
        } catch (Exception e) {
            log.error("Failed to change item status", e);
            throw new BusinessException("Failed to update item status");
        }
    }

    public void changeStatus(List<Item> items, ItemStatus status) {
        if (items == null) {
            throw new ValidationException("Items list cannot be null");
        }

        items.forEach(item -> changeStatus(item, status));
    }

    @Transactional(readOnly = true)
    public List<Item> getItemsByIds(List<Long> ids) {
        if (ids == null) {
            throw new ValidationException("Item IDs cannot be null");
        }

        try {
            return itemRepository.findAllByIdInAndItemStatus(ids, ACTIVE);
        } catch (Exception e) {
            log.error("Failed to get items by IDs", e);
            throw new BusinessException("Failed to retrieve items");
        }
    }

    @Transactional(readOnly = true)
    public Page<Item> getAllItems(int page, int size) {
        validatePagination(page, size);

        try {
            return itemRepository.findAllByItemStatus(ACTIVE, PageRequest.of(page, size));
        } catch (Exception e) {
            log.error("Failed to get items", e);
            throw new BusinessException("Failed to retrieve items");
        }
    }

    @Transactional(readOnly = true)
    public Item getItemById(Long id) {
        if (id == null) {
            throw new ValidationException("Item ID is required");
        }

        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Item> searchItems(String query, int page, int size) {
        validatePagination(page, size);

        try {
            String searchTerm = query != null ? query.trim() : "";
            Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

            if (searchTerm.isEmpty()) {
                return itemRepository.findAllByItemStatus(ACTIVE, pageable);
            }

            return itemRepository.findByItemStatusAndNameContainingIgnoreCaseOrItemStatusAndDescriptionContainingIgnoreCase(
                    ACTIVE, searchTerm, ACTIVE, searchTerm, pageable);

        } catch (Exception e) {
            log.error("Failed to search items", e);
            throw new BusinessException("Failed to search items");
        }
    }

    public Item createItem(Item item, MultipartFile[] images) {
        validateItemForCreation(item);

        try {
            Item savedItem = itemRepository.save(item);

            if (images != null && images.length > 0) {
                processItemImages(savedItem, images);
            }

            return savedItem;

        } catch (Exception e) {
            log.error("Failed to create item", e);
            throw new BusinessException("Failed to create item");
        }
    }

    public void deleteItem(Long id) {
        if (id == null) {
            throw new ValidationException("Item ID is required");
        }

        try {
            Item item = getItemById(id);
            item.setItemStatus(ItemStatus.DELETED);
            itemRepository.save(item);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to delete item", e);
            throw new BusinessException("Failed to delete item");
        }
    }

    private void processItemImages(Item item, MultipartFile[] images) {
        List<String> imageUrls = new ArrayList<>();

        for (int i = 0; i < images.length; i++) {
            MultipartFile image = images[i];
            if (image != null && !image.isEmpty()) {
                try {
                    String imageUrl = s3Service.uploadToS3(image, item, i);
                    imageUrls.add(imageUrl);
                } catch (Exception e) {
                    log.error("Failed to upload image for item {}", item.getId(), e);
                    throw new BusinessException("Failed to upload item images");
                }
            }
        }

        item.setNumberOfPictures(String.valueOf(imageUrls.size()));
        itemRepository.save(item);
    }

    private void validateItemForCreation(Item item) {
        if (item == null) {
            throw new ValidationException("Item cannot be null");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Item name is required");
        }
        if (item.getPrice() == null || item.getPrice().isBlank()) {
            throw new ValidationException("Item price is required");
        }
    }

    private void validateItemOperation(Item item, ItemStatus status) {
        if (item == null) {
            throw new ValidationException("Item cannot be null");
        }
        if (status == null) {
            throw new ValidationException("Status cannot be null");
        }
    }

    private void validatePagination(int page, int size) {
        if (page < 0) {
            throw new ValidationException("Page number cannot be negative");
        }
        if (size <= 0) {
            throw new ValidationException("Page size must be positive");
        }
    }

    public List<ItemResponse> findAll() {
        return itemRepository.findAll().stream()
                .map(itemMapper::toItemResponse)
                .collect(Collectors.toList());
    }

    public List<ItemResponse> toItemResponse(List<Item> items) {
        return items.stream()
                .map(itemMapper::toItemResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemResponse updateItem(Long id,
                                   ItemUpdateDto request) {
        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + id));

        itemMapper.updateFromDto(request, existingItem);

        Item updatedItem = itemRepository.save(existingItem);
        log.info("Item updated: {}", updatedItem.getId());

        return itemMapper.toItemResponse(updatedItem);
    }
}