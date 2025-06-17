package dev.wortel.meshok.service;

import dev.wortel.meshok.entity.Order;
import dev.wortel.meshok.error.ResourceNotFoundException;
import dev.wortel.meshok.error.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityService {
    private final OrderService orderService;

    public boolean hasAccessToOrder(Long orderId, Long userId,
                                    Collection<? extends GrantedAuthority> authorities) {
        if (orderId == null || userId == null) {
            throw new ValidationException("Order ID and User ID are required");
        }

        try {
            boolean isOwner = authorities.stream()
                    .anyMatch(a -> a.getAuthority().equals("OWNER"));

            if (isOwner) return true;

            Order order = orderService.getOrderById(orderId);
            return order.getUserId().equals(userId);

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Security check failed", e);
            throw new SecurityException("Access denied");
        }
    }
}