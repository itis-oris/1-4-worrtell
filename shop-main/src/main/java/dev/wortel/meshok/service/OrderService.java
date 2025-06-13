package dev.wortel.meshok.service;

import dev.wortel.meshok.dto.OrderDto;
import entity.Item;
import dev.wortel.meshok.entity.Order;
import dev.wortel.meshok.entity.OrderStatus;
import dev.wortel.meshok.exception.ResourceNotFoundException;
import dev.wortel.meshok.mapper.OrderMapper;
import dev.wortel.meshok.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static entity.ItemStatus.SOLD;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final OrderMapper orderMapper;
    private final ItemService itemService;

    @Transactional
    public Order createOrderFromCart(Long userId, OrderDto orderDTO) {
        List<Item> cartItems = cartService.getCartItems(userId);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Корзина пуста, невозможно создать заказ");
        }

        Order order = orderMapper.mapToOrder(orderDTO);
        order.setUserId(userId);
        order.setItems(cartItems);
        order.setTotalPrice(calculateTotalPrice(cartItems));

        order = orderRepository.save(order);

        cartService.clearCart(userId);
        updateOrderStatus(order.getId(), OrderStatus.PENDING);
        return order;
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ с ID " + orderId + " не найден"));
    }

    public List<Order> getUserOrders(Long userId) {
        log.info("orders {} by {}",orderRepository.findByUserIdOrderByOrderDateDesc(userId), userId);
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void updateOrderStatus(Long orderId, OrderStatus status)  {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        itemService.changeStatus(order.getItems(), SOLD);
        orderRepository.save(order);
    }

    private double calculateTotalPrice(List<Item> cartItems) {
        return cartItems.stream()
                .mapToDouble(item -> Double.parseDouble(item.getPrice()))
                .sum();
    }
}