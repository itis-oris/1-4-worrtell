package dev.wortel.meshok.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long meshok_id;
    @Column(length = 500)
    private String name;
    @Column(length = 500)
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
    @Column(length = 500)
    private String deliveryText;
    @Column(length = 2000)
    private String description;
}