package dev.wortel.meshok.repository;

import dev.wortel.meshok.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT p.id FROM Item p WHERE p.id IN :ids")
    List<Long> findExistingIds(@Param("ids") List<Long> ids);
}
