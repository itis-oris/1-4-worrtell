package dev.wortel.meshok.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItemDisplayDto {
    private Long id;
    private Long meshok_id;
    private String name;
    private String category;
    private String saleType;
    private String longevity;
    private String listDateTime;
    private String endDateTime;
    private String quantity;
    private String sold;
    private String localDelivery;
    private String localDeliveryPrice;
    private String delivery;
    private String countryDeliveryPrice;
    private String worldDeliveryPrice;
    private String condition;
    private String numberOfPictures;
    private String picturesFolderPath;
    private String city;
    private String status;
    private String price;
    private List<String> pictures;
}