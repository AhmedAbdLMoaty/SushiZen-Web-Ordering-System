package ism.lab02_ism.repository;

import ism.lab02_ism.entity.MenuItem;
import ism.lab02_ism.entity.Order;
import ism.lab02_ism.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);

    Optional<OrderItem> findByOrderAndMenuItem(Order order, MenuItem menuItem);
}