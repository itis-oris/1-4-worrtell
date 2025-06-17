package dev.wortel.meshok.dto;

import lombok.Data;

@Data
public class ItemRequest {
    Long id;
    String name;
    Double price;
    String description;
}