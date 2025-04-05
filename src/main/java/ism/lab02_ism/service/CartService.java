package ism.lab02_ism.service;

import ism.lab02_ism.entity.Cart;
import ism.lab02_ism.entity.CartItem;
import ism.lab02_ism.entity.MenuItem;
import ism.lab02_ism.entity.User;
import ism.lab02_ism.model.CartDTO;
import ism.lab02_ism.model.CartItemDTO;
import ism.lab02_ism.repository.CartItemRepository;
import ism.lab02_ism.repository.CartRepository;
import ism.lab02_ism.repository.MenuItemRepository;
import ism.lab02_ism.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
            UserRepository userRepository, MenuItemRepository menuItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional
    public CartDTO getUserCart(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Cart> cartOptional = cartRepository.findByUser(user);

            if (cartOptional.isPresent()) {
                return convertToDTO(cartOptional.get());
            } else {
                // Create a new cart for the user if one doesn't exist
                Cart newCart = new Cart();
                newCart.setCartId(UUID.randomUUID().toString());
                newCart.setUser(user);
                newCart.setTotalPrice(0);
                Cart savedCart = cartRepository.save(newCart);
                return convertToDTO(savedCart);
            }
        }

        return null;
    }

    @Transactional
    public boolean addItemToCart(String userId, CartItemDTO cartItemDTO) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        Optional<MenuItem> menuItemOptional = menuItemRepository.findByItemId(cartItemDTO.getItemId());

        if (userOptional.isPresent() && menuItemOptional.isPresent()) {
            User user = userOptional.get();
            MenuItem menuItem = menuItemOptional.get();

            Optional<Cart> cartOptional = cartRepository.findByUser(user);
            Cart cart;

            if (cartOptional.isPresent()) {
                cart = cartOptional.get();
            } else {
                cart = new Cart();
                cart.setCartId(UUID.randomUUID().toString());
                cart.setUser(user);
                cart.setTotalPrice(0);
                cart = cartRepository.save(cart);
            }

            Optional<CartItem> existingCartItemOptional = cartItemRepository.findByCartAndMenuItem(cart, menuItem);

            if (existingCartItemOptional.isPresent()) {
                CartItem existingItem = existingCartItemOptional.get();
                existingItem.setQuantity(cartItemDTO.getQuantity());
                existingItem.setTotalPrice(menuItem.getItemPrice() * cartItemDTO.getQuantity());
                cartItemRepository.save(existingItem);
            } else {
                CartItem newCartItem = new CartItem();
                newCartItem.setCart(cart);
                newCartItem.setMenuItem(menuItem);
                newCartItem.setQuantity(cartItemDTO.getQuantity());
                newCartItem.setTotalPrice(menuItem.getItemPrice() * cartItemDTO.getQuantity());
                cart.getItems().add(newCartItem);
                cartItemRepository.save(newCartItem);
            }

            updateCartTotal(cart);
            cartRepository.save(cart);

            return true;
        }

        return false;
    }

    @Transactional
    public boolean removeItemFromCart(String userId, String itemId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        Optional<MenuItem> menuItemOptional = menuItemRepository.findByItemId(itemId);

        if (userOptional.isPresent() && menuItemOptional.isPresent()) {
            User user = userOptional.get();
            MenuItem menuItem = menuItemOptional.get();

            Optional<Cart> cartOptional = cartRepository.findByUser(user);

            if (cartOptional.isPresent()) {
                Cart cart = cartOptional.get();
                Optional<CartItem> cartItemOptional = cartItemRepository.findByCartAndMenuItem(cart, menuItem);

                if (cartItemOptional.isPresent()) {
                    CartItem cartItem = cartItemOptional.get();
                    cart.getItems().remove(cartItem);
                    cartItemRepository.delete(cartItem);

                    updateCartTotal(cart);
                    cartRepository.save(cart);

                    return true;
                }
            }
        }

        return false;
    }

    private void updateCartTotal(Cart cart) {
        float total = 0;
        for (CartItem item : cart.getItems()) {
            total += item.getTotalPrice();
        }
        cart.setTotalPrice(total);
    }

    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setCartId(cart.getCartId());
        dto.setUserId(cart.getUser().getUserId());
        dto.setTotalPrice(cart.getTotalPrice());

        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }

    private CartItemDTO convertToDTO(CartItem cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setItemId(cartItem.getMenuItem().getItemId());
        dto.setQuantity(cartItem.getQuantity());

        dto.setTotalPrice(new java.math.BigDecimal(Float.toString(cartItem.getTotalPrice())));
        return dto;
    }
}