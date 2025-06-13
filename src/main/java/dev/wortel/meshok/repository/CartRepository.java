package dev.wortel.meshok.repository;

import dev.wortel.meshok.entity.Cart;
import dev.wortel.meshok.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserId(Long userId);

    Optional<Cart> findByUserIdAndItemId(Long userId, Long itemId);

    void deleteByUserIdAndItemId(Long userId, Long itemId);

    void deleteByUserId(Long userId);

    boolean existsByUserIdAndItemId(Long userId, Long itemId);
}