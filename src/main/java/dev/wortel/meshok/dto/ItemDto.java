package dev.wortel.meshok.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDto {
    private Long id;
    private String name;
    private Long category;
    private String saleType;
    private String longevity;
    private String listDateTime;
    private String endDateTime;
    private String quantity;
    private String sold; //check "sold": "0"
    private String localDelivery;
    private String localDeliveryPrice;
    private String delivery;
    private String countryDeliveryPrice;
    private String worldDeliveryPrice;
    private String condition;
    private String numberOfPictures;
    private String city;
    private String status;
    private String price;
    private String deliveryText;
    private String description;
}
