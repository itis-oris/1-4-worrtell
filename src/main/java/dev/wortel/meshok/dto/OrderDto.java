package dev.wortel.meshok.dto;

import dev.wortel.meshok.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private String deliveryAddress;
    private PaymentMethod paymentMethod;
    private String comment;
}