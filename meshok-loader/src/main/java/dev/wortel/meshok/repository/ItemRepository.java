package dev.wortel.meshok.repository;

import entity.Item;
import entity.ItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT p.id FROM Item p WHERE p.id IN :ids")
    List<Long> findExistingIds(@Param("ids") List<Long> ids);

    List<Item> findAllByIdInAndItemStatus(List<Long> ids, ItemStatus itemStatus);

    Page<Item> findAllByItemStatus(ItemStatus itemStatus, Pageable pageable);

    Page<Item> findByItemStatusAndCategory(ItemStatus itemStatus, String category, Pageable pageable);

    Page<Item> findByItemStatusAndNameContainingIgnoreCaseOrItemStatusAndDescriptionContainingIgnoreCase(
            ItemStatus itemStatus1, String name,
            ItemStatus itemStatus2, String description,
            Pageable pageable
    );

    Optional<Item> findByMeshokIdAndItemStatus(Long meshokId, ItemStatus itemStatus);
}