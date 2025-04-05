package ism.lab02_ism.repository;

import ism.lab02_ism.entity.Cart;
import ism.lab02_ism.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCartId(String cartId);

    Optional<Cart> findByUser(User user);
}