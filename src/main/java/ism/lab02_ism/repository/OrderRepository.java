package ism.lab02_ism.repository;

import ism.lab02_ism.entity.Order;
import ism.lab02_ism.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderId(String orderId);

    List<Order> findByUser(User user);

    List<Order> findByStatus(Order.OrderStatus status);
}