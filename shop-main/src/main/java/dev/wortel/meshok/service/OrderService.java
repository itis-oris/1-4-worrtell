package dev.wortel.meshok.service;

import dev.wortel.meshok.dto.OrderDto;
import dev.wortel.meshok.error.BaseException;
import dev.wortel.meshok.error.BusinessException;
import dev.wortel.meshok.error.ResourceNotFoundException;
import dev.wortel.meshok.error.ValidationException;
import entity.Item;
import dev.wortel.meshok.entity.Order;
import dev.wortel.meshok.entity.OrderStatus;
import dev.wortel.meshok.mapper.OrderMapper;
import dev.wortel.meshok.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static entity.ItemStatus.SOLD;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final OrderMapper orderMapper;
    private final ItemService itemService;

    public Order createOrderFromCart(Long userId, OrderDto orderDTO) {
        validateOrderCreation(userId, orderDTO);

        try {
            List<Item> cartItems = cartService.getCartItems(userId);
            if (cartItems.isEmpty()) {
                throw new ValidationException("Cannot create order from empty cart");
            }

            Order order = buildOrder(orderDTO, userId, cartItems);
            Order savedOrder = orderRepository.save(order);

            cartService.clearCart(userId);
            updateOrderStatus(savedOrder.getId(), OrderStatus.PENDING);

            return savedOrder;

        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to create order", e);
            throw new BusinessException("Failed to create order");
        }
    }

    public Order getOrderById(Long orderId) {
        if (orderId == null) {
            throw new ValidationException("Order ID is required");
        }
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    public List<Order> getUserOrders(Long userId) {
        if (userId == null) {
            throw new ValidationException("User ID is required");
        }
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void updateOrderStatus(Long orderId, OrderStatus status) {
        try {
            Order order = getOrderById(orderId);
            order.setStatus(status);
            itemService.changeStatus(order.getItems(), SOLD);
            orderRepository.save(order);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to update order status", e);
            throw new BusinessException("Failed to update order status");
        }
    }

    private Order buildOrder(OrderDto orderDTO, Long userId, List<Item> cartItems) {
        Order order = orderMapper.mapToOrder(orderDTO);
        order.setUserId(userId);
        order.setItems(cartItems);
        order.setTotalPrice(calculateTotalPrice(cartItems));
        return order;
    }

    private double calculateTotalPrice(List<Item> cartItems) {
        return cartItems.stream()
                .mapToDouble(item -> {
                    try {
                        return Double.parseDouble(item.getPrice());
                    } catch (NumberFormatException e) {
                        throw new BusinessException("Invalid item price format");
                    }
                })
                .sum();
    }

    private void validateOrderCreation(Long userId, OrderDto orderDTO) {
        if (userId == null) {
            throw new ValidationException("User ID is required");
        }
        if (orderDTO == null) {
            throw new ValidationException("Order data is required");
        }
    }
}