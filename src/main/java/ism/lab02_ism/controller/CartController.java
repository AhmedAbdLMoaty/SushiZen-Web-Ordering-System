package ism.lab02_ism.controller;

import ism.lab02_ism.api.CartApi;
import ism.lab02_ism.model.CartDTO;
import ism.lab02_ism.model.CartItemDTO;
import ism.lab02_ism.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController implements CartApi {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public ResponseEntity<CartDTO> getUserCart() {

        String userId = "1";

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CartDTO cart = cartService.getUserCart(userId);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<Void> addItemToCart(CartItemDTO cartItemDTO) {

        String userId = "1";

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (cartItemDTO.getItemId() == null || cartItemDTO.getQuantity() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (cartItemDTO.getQuantity() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        boolean success = cartService.addItemToCart(userId, cartItemDTO);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<Void> removeItemFromCart(String itemId) {

        String userId = "1";

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (itemId == null || itemId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        boolean success = cartService.removeItemFromCart(userId, itemId);
        if (success) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}