package dev.wortel.meshok.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ItemUpdateDto {
    @NotBlank(message = "Название товара обязательно")
    @Size(min = 3, max = 100, message = "Название должно быть от 3 до 100 символов")
    private String name;

    @NotBlank
    @Size(min = 2, max = 50, message = "Категория должна быть от 2 до 50 символов")
    private String category;

    @NotBlank
    @Min(value = 1, message = "Количество должно быть не менее 1")
    private String quantity;

    @NotBlank
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "Цена должна быть числом с максимум 2 знаками после запятой")
    private String price;

    @Size(max = 2000, message = "Описание не должно превышать 2000 символов")
    private String description;
}