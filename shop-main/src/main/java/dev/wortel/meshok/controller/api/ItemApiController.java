package dev.wortel.meshok.controller.api;

import dev.wortel.meshok.dto.ItemCreateDto;
import dev.wortel.meshok.dto.ItemResponse;
import dev.wortel.meshok.dto.ItemUpdateDto;
import dev.wortel.meshok.mapper.ItemMapper;
import dev.wortel.meshok.service.ItemService;
import entity.Item;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemApiController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAll() {
        return ResponseEntity.ok(itemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(itemMapper.toItemResponse(itemService.getItemById(id)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemResponse> createItem(
            @RequestPart @Valid ItemCreateDto request,
            @RequestPart(required = false) MultipartFile[] images) {

        Item item = itemService.createItem(itemMapper.toEntity(request), images);
        ItemResponse createdItem = itemMapper.toItemResponse(item);
        return ResponseEntity
                .created(URI.create("/api/items/" + item.getId()))
                .body(createdItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ItemUpdateDto request) {
        return ResponseEntity.ok(itemService.updateItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}