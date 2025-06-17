package dev.wortel.meshok.controller;

import dev.wortel.meshok.dto.ItemCreateDto;
import dev.wortel.meshok.dto.ItemDisplayDto;
import entity.Item;
import dev.wortel.meshok.helper.PictureHelper;
import dev.wortel.meshok.mapper.ItemMapper;
import dev.wortel.meshok.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final PictureHelper pictureHelper;

    @GetMapping
    public Object getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false, defaultValue = "false") boolean isAjax,
            Model model,
            HttpServletRequest request) {
        log.info("Get all items");

        try {
            Page<Item> itemPage = itemService.getAllItems(page, size);
            List<ItemDisplayDto> displayDtos = itemPage.getContent().stream()
                    .map(item -> itemMapper.toItemDisplayDto(item, pictureHelper))
                    .toList();

            if (isAjax || isJsonRequest(request)) {
                Map<String, Object> response = new HashMap<>();
                response.put("items", displayDtos);
                response.put("hasNextPage", itemPage.hasNext());
                return ResponseEntity.ok(response);
            }

            model.addAttribute("items", displayDtos);
            model.addAttribute("currentPage", page);
            model.addAttribute("hasNextPage", itemPage.hasNext());
            return "items/list";

        } catch (Exception e) {
            return handleException(e, request);
        }
    }

    @GetMapping("/{id}")
    public Object getItemById(@PathVariable Long id, Model model, HttpServletRequest request) {
        log.info("Get item by ID {}", id);
        try {
            Item item = itemService.getItemById(id);
            ItemDisplayDto dto = itemMapper.toItemDisplayDto(item, pictureHelper);

            if (isJsonRequest(request)) {
                return ResponseEntity.ok(dto);
            }

            model.addAttribute("item", dto);
            return "items/details";

        } catch (Exception e) {
            return handleException(e, request);
        }
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

    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("item", new ItemCreateDto());
        return "items/create";
    }

    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
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

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/{id}/delete")
    public String deleteItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            itemService.deleteItem(id);
            log.info("Deleted item with ID: {}", id);
            redirectAttributes.addFlashAttribute("successMessage", "Лот успешно удален!");
            return "redirect:/items";
        } catch (Exception e) {
            log.error("Error deleting item", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении лота: " + e.getMessage());
            return "redirect:/items/" + id;
        }
    }

    private boolean isJsonRequest(HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        return acceptHeader != null && acceptHeader.contains("application/json");
    }

    private Object handleException(Exception e, HttpServletRequest request) {
        log.error("Error processing request", e);

        if (isJsonRequest(request)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
    }
}
