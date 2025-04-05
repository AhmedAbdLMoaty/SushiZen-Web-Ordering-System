package ism.lab02_ism.repository;

import ism.lab02_ism.entity.Cart;
import ism.lab02_ism.entity.CartItem;
import ism.lab02_ism.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);

    Optional<CartItem> findByCartAndMenuItem(Cart cart, MenuItem menuItem);
}