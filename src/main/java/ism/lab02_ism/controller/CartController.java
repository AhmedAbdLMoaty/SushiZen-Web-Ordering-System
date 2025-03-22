package ism.lab02_ism.controller;

import ism.lab02_ism.api.CartApi;
import ism.lab02_ism.model.CartDTO;
import ism.lab02_ism.model.CartItemDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class CartController implements CartApi {

    private final Map<String, CartDTO> carts = new HashMap<>();

    @Override
    public ResponseEntity<CartDTO> getUserCart() {
        
        String userId = "1";
        CartDTO cart = carts.computeIfAbsent(userId, this::createNewCart);
        return ResponseEntity.ok(cart);
    }

    @Override
    public ResponseEntity<Void> addItemToCart(CartItemDTO cartItemDTO) {
        String userId = "1"; 
        CartDTO cart = carts.computeIfAbsent(userId, this::createNewCart);
        
        
        boolean found = false;
        for (CartItemDTO item : cart.getItems()) {
            if (item.getItemId().equals(cartItemDTO.getItemId())) {
                
                item.setQuantity(item.getQuantity() + cartItemDTO.getQuantity());
                
                BigDecimal unitPrice = new BigDecimal("10.00");
                BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
                item.setTotalPrice(unitPrice.multiply(quantity));
                
                found = true;
                break;
            }
        }
        
        if (!found) {
            cart.getItems().add(cartItemDTO);
        }
        
        
        updateCartTotal(cart);
        
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> removeItemFromCart(String itemId) {
        String userId = "1"; 
        CartDTO cart = carts.get(userId);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        
        
        cart.getItems().removeIf(item -> item.getItemId().equals(itemId));
        
        
        updateCartTotal(cart);
        
        return ResponseEntity.ok().build();
    }
    
    private CartDTO createNewCart(String userId) {
        CartDTO cart = new CartDTO();
        cart.setCartId(UUID.randomUUID().toString());
        cart.setUserId(userId);
        cart.setItems(new ArrayList<>());
        cart.setTotalPrice(0.0f); 
        return cart;
    }
    
    private void updateCartTotal(CartDTO cart) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItemDTO item : cart.getItems()) {
            
            total = total.add(item.getTotalPrice());
        }
        cart.setTotalPrice(total.floatValue()); 
    }
}