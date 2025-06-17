package dev.wortel.meshok.service;

import dev.wortel.meshok.entity.Cart;
import dev.wortel.meshok.error.BusinessException;
import dev.wortel.meshok.error.ResourceNotFoundException;
import dev.wortel.meshok.error.ValidationException;
import entity.Item;
import dev.wortel.meshok.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartEntryRepository;
    private final ItemService itemService;

    public void addToCart(Long userId, Long itemId) {
        validateCartOperation(userId, itemId);

        try {
            if (!cartEntryRepository.existsByUserIdAndItemId(userId, itemId)) {
                Cart entry = new Cart();
                entry.setUserId(userId);
                entry.setItemId(itemId);
                cartEntryRepository.save(entry);
            }
        } catch (Exception e) {
            log.error("Failed to add item to cart", e);
            throw new BusinessException("Failed to add item to cart");
        }
    }

    public void removeFromCart(Long userId, Long itemId) {
        validateCartOperation(userId, itemId);

        try {
            int deleted = cartEntryRepository.deleteByUserIdAndItemId(userId, itemId);
            if (deleted == 0) {
                throw new ResourceNotFoundException("Item not found in cart");
            }
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to remove item from cart", e);
            throw new BusinessException("Failed to remove item from cart");
        }
    }

    public void clearCart(Long userId) {
        if (userId == null) {
            throw new ValidationException("User ID is required");
        }

        try {
            cartEntryRepository.deleteByUserId(userId);
        } catch (Exception e) {
            log.error("Failed to clear cart", e);
            throw new BusinessException("Failed to clear cart");
        }
    }

    @Transactional(readOnly = true)
    public List<Item> getCartItems(Long userId) {
        if (userId == null) {
            throw new ValidationException("User ID is required");
        }

        try {
            List<Cart> entries = cartEntryRepository.findByUserId(userId);
            List<Long> itemIds = entries.stream()
                    .map(Cart::getItemId)
                    .toList();

            return itemIds.isEmpty() ?
                    Collections.emptyList() :
                    itemService.getItemsByIds(itemIds);

        } catch (Exception e) {
            log.error("Failed to get cart items", e);
            throw new BusinessException("Failed to retrieve cart items");
        }
    }

    @Transactional(readOnly = true)
    public boolean isItemInCart(Long userId, Long itemId) {
        validateCartOperation(userId, itemId);

        try {
            return cartEntryRepository.existsByUserIdAndItemId(userId, itemId);
        } catch (Exception e) {
            log.error("Failed to check cart item", e);
            throw new BusinessException("Failed to check cart item");
        }
    }

    @Transactional(readOnly = true)
    public int getCartSize(Long userId) {
        if (userId == null) {
            throw new ValidationException("User ID is required");
        }

        try {
            return cartEntryRepository.findByUserId(userId).size();
        } catch (Exception e) {
            log.error("Failed to get cart size", e);
            throw new BusinessException("Failed to get cart size");
        }
    }

    private void validateCartOperation(Long userId, Long itemId) {
        if (userId == null || itemId == null) {
            throw new ValidationException("User ID and Item ID are required");
        }
    }
}