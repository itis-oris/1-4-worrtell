package dev.wortel.meshok.repository;

import dev.wortel.meshok.entity.Order;
import dev.wortel.meshok.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    List<Order> findByUserIdOrderByOrderDateDesc(Long userId);

    List<Order> findAllByStatus(OrderStatus status);
}