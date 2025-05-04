package ism.lab02_ism.controller;

import ism.lab02_ism.api.CartApi;
import ism.lab02_ism.model.CartDTO;
import ism.lab02_ism.model.CartItemDTO;
import ism.lab02_ism.service.CartService;
import ism.lab02_ism.util.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController implements CartApi {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;
    private final AuthUtils authUtils;

    @Autowired
    public CartController(CartService cartService, AuthUtils authUtils) {
        this.cartService = cartService;
        this.authUtils = authUtils;
    }

    @GetMapping("/cart/{userId}")
    public ResponseEntity<CartDTO> getUserCartById(@PathVariable String userId) {
        String currentUserId = authUtils.getCurrentUserId();

        // Only admins can access other users' carts
        if (!userId.equals(currentUserId) && !authUtils.isAdmin()) {
            logger.warn("User {} attempted to access cart of user {}", currentUserId, userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            logger.debug("Fetching cart for user: {}", userId);
            CartDTO cart = cartService.getUserCart(userId);
            if (cart != null) {
                return ResponseEntity.ok(cart);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error retrieving cart for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<CartDTO> getUserCart() {
        String userId = authUtils.getCurrentUserId();

        if (userId == null) {
            logger.warn("Unauthorized access attempt to cart");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            logger.debug("Fetching cart for user: {}", userId);
            CartDTO cart = cartService.getUserCart(userId);
            if (cart != null) {
                return ResponseEntity.ok(cart);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error retrieving cart for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/cart/{userId}/items")
    public ResponseEntity<Void> addItemToCartForUser(@PathVariable String userId,
            @RequestBody CartItemDTO cartItemDTO) {
        String currentUserId = authUtils.getCurrentUserId();

        // Only admins can add items to other users' carts
        if (!userId.equals(currentUserId) && !authUtils.isAdmin()) {
            logger.warn("User {} attempted to add item to cart of user {}", currentUserId, userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (cartItemDTO == null || cartItemDTO.getItemId() == null || cartItemDTO.getQuantity() == null) {
            logger.warn("Invalid cart item data received");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (cartItemDTO.getQuantity() <= 0) {
            logger.warn("Invalid quantity received: {}", cartItemDTO.getQuantity());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            logger.debug("Adding item to cart for user: {}", userId);
            boolean success = cartService.addItemToCart(userId, cartItemDTO);
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error adding item to cart for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> addItemToCart(CartItemDTO cartItemDTO) {
        String userId = authUtils.getCurrentUserId();

        if (userId == null) {
            logger.warn("Unauthorized access attempt to add item to cart");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (cartItemDTO == null || cartItemDTO.getItemId() == null || cartItemDTO.getQuantity() == null) {
            logger.warn("Invalid cart item data received");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (cartItemDTO.getQuantity() <= 0) {
            logger.warn("Invalid quantity received: {}", cartItemDTO.getQuantity());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            logger.debug("Adding item to cart for user: {}", userId);
            boolean success = cartService.addItemToCart(userId, cartItemDTO);
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error adding item to cart for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/cart/{userId}/items/{itemId}")
    public ResponseEntity<Void> removeItemFromCartForUser(@PathVariable String userId, @PathVariable String itemId) {
        String currentUserId = authUtils.getCurrentUserId();

        // Only admins can remove items from other users' carts
        if (!userId.equals(currentUserId) && !authUtils.isAdmin()) {
            logger.warn("User {} attempted to remove item from cart of user {}", currentUserId, userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (itemId == null || itemId.isEmpty()) {
            logger.warn("Invalid item ID received");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            logger.debug("Removing item {} from cart for user: {}", itemId, userId);
            boolean success = cartService.removeItemFromCart(userId, itemId);
            if (success) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error removing item from cart for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> removeItemFromCart(String itemId) {
        String userId = authUtils.getCurrentUserId();

        if (userId == null) {
            logger.warn("Unauthorized access attempt to remove item from cart");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (itemId == null || itemId.isEmpty()) {
            logger.warn("Invalid item ID received");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            logger.debug("Removing item {} from cart for user: {}", itemId, userId);
            boolean success = cartService.removeItemFromCart(userId, itemId);
            if (success) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error removing item from cart for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}