package dev.wortel.meshok.service;

import dev.wortel.meshok.dto.OrderDto;
import dev.wortel.meshok.entity.Item;
import dev.wortel.meshok.entity.Order;
import dev.wortel.meshok.entity.OrderStatus;
import dev.wortel.meshok.exception.ResourceNotFoundException;
import dev.wortel.meshok.mapper.OrderMapper;
import dev.wortel.meshok.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dev.wortel.meshok.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ItemRepository itemRepository;
    //private final EmailService emailService;
    private final UserService userRepository;
    private final OrderMapper orderMapper;

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

        cartService.clearCart(userId);

//        // Отправляем письмо с подтверждением
//        if (orderDTO.getContactEmail() != null && !orderDTO.getContactEmail().isEmpty()) {
//            emailService.sendOrderConfirmation(savedOrder, orderDTO.getContactEmail());
//        } else {
//            // Попытка найти email пользователя
//            userRepository.findById(userId).ifPresent(user -> {
//                if (user.getEmail() != null && !user.getEmail().isEmpty()) {
//                    emailService.sendOrderConfirmation(savedOrder, user.getEmail());
//                }
//            });
//        }

        return orderRepository.save(order);
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

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status)  {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    private double calculateTotalPrice(List<Item> cartItems) {
        return cartItems.stream()
                .mapToDouble(item -> Double.parseDouble(item.getPrice()))
                .sum();
    }
}