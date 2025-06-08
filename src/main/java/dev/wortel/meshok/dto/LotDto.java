package dev.wortel.meshok.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LotDto {
    Long id;
//    String saleType;
//    String quantity;
//    String sold;
//    String curencyId;
//    String price;
}