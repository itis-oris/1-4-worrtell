package dev.wortel.meshok.service;

import dev.wortel.meshok.entity.Cart;
import dev.wortel.meshok.entity.Item;
import dev.wortel.meshok.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartEntryRepository;
    private final ItemService itemRepository;

    public void addToCart(Long userId, Long itemId) {
        if (!cartEntryRepository.existsByUserIdAndItemId(userId, itemId)) {
            Cart entry = new Cart();
            entry.setUserId(userId);
            entry.setItemId(itemId);
            cartEntryRepository.save(entry);
        }
    }

    @Transactional
    public void removeFromCart(Long userId, Long itemId) {
        log.info("Removing cart from cart repository {}, {}", userId, itemId);
        cartEntryRepository.deleteByUserIdAndItemId(userId, itemId);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartEntryRepository.deleteByUserId(userId);
    }

    public List<Item> getCartItems(Long userId) {
        List<Cart> entries = cartEntryRepository.findByUserId(userId);

        List<Long> itemIds = entries.stream()
                .map(Cart::getItemId)
                .toList();

        if (itemIds.isEmpty()) {
            return Collections.emptyList();
        }

        return itemRepository.getItemsByIds(itemIds);
    }

    public boolean isItemInCart(Long userId, Long itemId) {
        return cartEntryRepository.existsByUserIdAndItemId(userId, itemId);
    }

    public int getCartSize(Long userId) {
        return cartEntryRepository.findByUserId(userId).size();
    }
}