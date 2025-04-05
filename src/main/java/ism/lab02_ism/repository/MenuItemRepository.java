package ism.lab02_ism.repository;

import ism.lab02_ism.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    Optional<MenuItem> findByItemId(String itemId);

    boolean existsByItemId(String itemId);
}