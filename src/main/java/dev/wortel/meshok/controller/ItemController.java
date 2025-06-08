package dev.wortel.meshok.controller;

import dev.wortel.meshok.entity.Item;
import dev.wortel.meshok.entity.User;
import dev.wortel.meshok.repository.ItemRepository;
import dev.wortel.meshok.security.details.UserDetailsImpl;
import dev.wortel.meshok.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;

    @GetMapping
    public String getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {
        Page<Item> itemPage = itemService.getAllItems(page, size);
        model.addAttribute("items", itemPage.getContent());
        return "items/list";
    }

    @GetMapping("/{id}")
    public String getItemById(@PathVariable Long id, Model model) {
        Item item = itemService.getItemById(id);
        model.addAttribute("item", item);
        return "items/details";
    }

    @GetMapping("/search")
    public String searchItems(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {
        Page<Item> itemPage = itemService.searchItems(query, page, size);
        model.addAttribute("items", itemPage.getContent());
        model.addAttribute("searchQuery", query);
        log.info("Search {}", query);
        return "items/list";
    }

//    @GetMapping("/profile")
//    public String showProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        User user = userDetails.getUser();
//        // ...
//    }
//
//    @GetMapping("/cart")
//    public String showCart(Principal principal, Model model) {
//        // Здесь должна быть логика получения корзины пользователя
//        // Например:
//        // Cart cart = cartService.getUserCart(principal.getName());
//        // model.addAttribute("cart", cart);
//        return "cart/view";
//    }
}