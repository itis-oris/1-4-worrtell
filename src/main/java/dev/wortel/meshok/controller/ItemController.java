package dev.wortel.meshok.controller;

import dev.wortel.meshok.dto.ItemCreateDto;
import dev.wortel.meshok.dto.ItemDisplayDto;
import dev.wortel.meshok.entity.Item;
import dev.wortel.meshok.entity.User;
import dev.wortel.meshok.helper.PictureHelper;
import dev.wortel.meshok.mapper.ItemMapper;
import dev.wortel.meshok.repository.ItemRepository;
import dev.wortel.meshok.security.details.UserDetailsImpl;
import dev.wortel.meshok.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final PictureHelper pictureHelper;

    @GetMapping
    public String getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {
        Page<Item> itemPage = itemService.getAllItems(page, size);
        List<ItemDisplayDto> displayDtos = itemPage.getContent().stream()
                .map(item -> itemMapper.toItemDisplayDto(item, pictureHelper))
                .toList();
        model.addAttribute("items", displayDtos);
        return "items/list";
    }

    @GetMapping("/{id}")
    public String getItemById(@PathVariable Long id, Model model) {
        Item item = itemService.getItemById(id);
        ItemDisplayDto dto = itemMapper.toItemDisplayDto(item, pictureHelper);
        model.addAttribute("item", dto);
        return "items/details";
    }

    @GetMapping("/search")
    public String searchItems(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {
        Page<Item> itemPage = itemService.searchItems(query, page, size);
        List<ItemDisplayDto> displayDtos = itemPage.getContent().stream()
                .map(item -> itemMapper.toItemDisplayDto(item, pictureHelper))
                .toList();
        model.addAttribute("items", displayDtos);
        model.addAttribute("searchQuery", query);
        log.info("Search {}", query);
        return "items/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("item", new ItemCreateDto());
        return "items/create";
    }

    @PostMapping("/create")
    public String createItem(@ModelAttribute("item") @Valid ItemCreateDto itemDto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "items/create";
        }

        try {
            Item savedItem = itemService.createItem(itemMapper.toEntity(itemDto), itemDto.getImages());
            ItemDisplayDto item = itemMapper.fromCreateToItemDisplayDto(itemDto, Integer.parseInt(savedItem.getNumberOfPictures()),savedItem.getId(), pictureHelper);
            log.info("Create item {}", item);

            log.info("Created new item with ID: {}", savedItem.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Лот успешно создан!");

            return "redirect:/items/" + savedItem.getId();
        } catch (Exception e) {
            log.error("Error creating item", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при создании лота: " + e.getMessage());
            return "redirect:/items/create";
        }
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