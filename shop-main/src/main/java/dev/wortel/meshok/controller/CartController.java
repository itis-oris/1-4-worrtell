package dev.wortel.meshok.controller;

import dev.wortel.meshok.dto.ItemDisplayDto;
import dev.wortel.meshok.entity.Item;
import dev.wortel.meshok.helper.PictureHelper;
import dev.wortel.meshok.mapper.ItemMapper;
import dev.wortel.meshok.service.CartService;
import dev.wortel.meshok.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final ItemMapper itemMapper;
    private final PictureHelper pictureHelper;
    private final UserService userService;

    @GetMapping
    public String viewCart(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        // Получаем ID пользователя из Principal
        Long userId = getUserId(principal);

        // Получаем товары в корзине
        List<Item> cartItems = cartService.getCartItems(userId);

        // Конвертируем в DTO для отображения
        List<ItemDisplayDto> displayDtos = cartItems.stream()
                .map(item -> itemMapper.toItemDisplayDto(item, pictureHelper))
                .toList();

        model.addAttribute("cartItems", displayDtos);
        return "cart/view";
    }

    @PostMapping("/add/{itemId}")
    public String addToCart(
            @PathVariable Long itemId,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            Long userId = getUserId(principal);
            cartService.addToCart(userId, itemId);
            redirectAttributes.addFlashAttribute("successMessage", "Товар добавлен в корзину");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при добавлении товара в корзину");
        }
        return "redirect:/items/" + itemId;
    }

    @Transactional
    @PostMapping("/remove/{itemId}")
    public String removeFromCart(
            @PathVariable Long itemId,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            Long userId = getUserId(principal);
            log.info("Removing from cart {}", userId);
            cartService.removeFromCart(userId, itemId);
            redirectAttributes.addFlashAttribute("successMessage", "Товар удален из корзины");
        } catch (Exception e) {
            log.info(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении товара из корзины");
        }
        return "redirect:/cart";
    }

    @Transactional
    @PostMapping("/clear")
    public String clearCart(
            Principal principal,
            RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            Long userId = getUserId(principal);
            cartService.clearCart(userId);
            redirectAttributes.addFlashAttribute("successMessage", "Корзина очищена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при очистке корзины");
        }
        return "redirect:/cart";
    }

    private Long getUserId(Principal principal) {
        return userService.findByEmail(principal.getName()).getId();
    }
}