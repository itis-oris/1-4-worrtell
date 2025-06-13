package dev.wortel.meshok.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ItemCreateDto {
    private String name;
    private String category;
    private String saleType;
    private String longevity;
    private String quantity;
    private String localDelivery;
    private String localDeliveryPrice;
    private String delivery;
    private String countryDeliveryPrice;
    private String worldDeliveryPrice;
    private String condition;
    private String city;
    private String price;
    private String deliveryText;
    private String description;
    private MultipartFile[] images;
}