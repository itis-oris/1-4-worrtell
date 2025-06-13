package dev.wortel.meshok.mapper;

import dev.wortel.meshok.dto.OrderDto;
import dev.wortel.meshok.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", constant = "PENDING")
    Order mapToOrder(OrderDto orderDTO);
}