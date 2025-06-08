package dev.wortel.meshok.controller;

import dev.wortel.meshok.entity.Item;
import dev.wortel.meshok.repository.ItemRepository;
import dev.wortel.meshok.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        model.addAttribute("item", item);
        return "items/details";
    }

//    @GetMapping("/category/{category}")
//    public String getItemsByCategory(@PathVariable String category, Model model) {
//        List<Item> items = itemRepository.findByCategory(category);
//        model.addAttribute("items", items);
//        model.addAttribute("category", category);
//        return "items/category";
//    }
}