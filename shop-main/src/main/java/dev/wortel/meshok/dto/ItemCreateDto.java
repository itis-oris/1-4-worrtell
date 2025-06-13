package dev.wortel.meshok.dto;

import dev.wortel.meshok.validator.ValidImage;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.*;

@Data
public class ItemCreateDto {
    @NotBlank(message = "Название товара обязательно")
    @Size(min = 3, max = 100, message = "Название должно быть от 3 до 100 символов")
    private String name;

    @NotBlank(message = "Категория товара обязательна")
    @Size(min = 2, max = 50, message = "Категория должна быть от 2 до 50 символов")
    private String category;

    private String saleType;
    private String longevity;

    @Min(value = 1, message = "Количество должно быть не менее 1")
    private String quantity;

    private String localDelivery;
    private String localDeliveryPrice;
    private String delivery;
    private String countryDeliveryPrice;
    private String worldDeliveryPrice;

    @NotBlank(message = "Укажите состояние товара")
    private String condition;

    @NotBlank(message = "Укажите город")
    private String city;

    @NotBlank(message = "Укажите цену")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "Цена должна быть числом с максимум 2 знаками после запятой")
    private String price;

    private String deliveryText;

    @Size(max = 2000, message = "Описание не должно превышать 2000 символов")
    private String description;

    @NotNull(message = "Загрузите хотя бы одно изображение")
    @ValidImage
    @Size(min = 1, max = 9, message = "Можно загрузить от 1 до 9 изображений")
    private MultipartFile[] images;
}