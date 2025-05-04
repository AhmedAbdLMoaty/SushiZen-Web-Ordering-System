package ism.lab02_ism.service;

import ism.lab02_ism.entity.Cart;
import ism.lab02_ism.entity.MenuItem;
import ism.lab02_ism.entity.Order;
import ism.lab02_ism.entity.OrderItem;
import ism.lab02_ism.entity.User;
import ism.lab02_ism.model.CartItemDTO;
import ism.lab02_ism.model.OrderDTO;
import ism.lab02_ism.repository.CartItemRepository;
import ism.lab02_ism.repository.CartRepository;
import ism.lab02_ism.repository.MenuItemRepository;
import ism.lab02_ism.repository.OrderItemRepository;
import ism.lab02_ism.repository.OrderRepository;
import ism.lab02_ism.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
            UserRepository userRepository, MenuItemRepository menuItemRepository,
            CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.menuItemRepository = menuItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public OrderDTO placeOrder(OrderDTO orderDTO) {
        Optional<User> userOptional = userRepository.findByUserId(orderDTO.getUserId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Order order = new Order();
            order.setOrderId(UUID.randomUUID().toString());
            order.setUser(user);
            order.setStatus(Order.OrderStatus.PENDING);
            order.setPaymentStatus(Order.PaymentStatus.UNPAID);
            order.setCreatedAt(LocalDateTime.now());

            order = orderRepository.save(order);

            float totalPrice = 0;

            for (CartItemDTO itemDTO : orderDTO.getItems()) {
                Optional<MenuItem> menuItemOptional = menuItemRepository.findByItemId(itemDTO.getItemId());

                if (menuItemOptional.isPresent()) {
                    MenuItem menuItem = menuItemOptional.get();

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setMenuItem(menuItem);
                    orderItem.setQuantity(itemDTO.getQuantity());

                    float itemTotal = menuItem.getItemPrice() * itemDTO.getQuantity();
                    orderItem.setTotalPrice(java.math.BigDecimal.valueOf(itemTotal));

                    orderItemRepository.save(orderItem);

                    totalPrice += itemTotal;
                }
            }

            order.setTotalPrice(totalPrice);
            orderRepository.save(order);

            // Clear the user's cart
            Optional<Cart> cartOptional = cartRepository.findByUser(user);
            if (cartOptional.isPresent()) {
                Cart cart = cartOptional.get();
                cartItemRepository.deleteAll(cart.getItems());
                cart.getItems().clear();
                cart.setTotalPrice(0);
                cartRepository.save(cart);
            }

            return convertToDTO(order);
        }

        return null;
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrderById(String orderId) {
        Optional<Order> orderOptional = orderRepository.findByOrderId(orderId);
        return orderOptional.map(this::convertToDTO).orElse(null);
    }

    @Transactional(readOnly = true)
    public Map<String, OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        Map<String, OrderDTO> orderDTOs = new HashMap<>();

        for (Order order : orders) {
            OrderDTO dto = convertToDTO(order);
            orderDTOs.put(dto.getOrderId(), dto);
        }

        return orderDTOs;
    }

    @Transactional
    public boolean updateOrderStatus(String orderId, OrderDTO.StatusEnum statusEnum) {
        Optional<Order> orderOptional = orderRepository.findByOrderId(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            switch (statusEnum) {
                case PENDING:
                    order.setStatus(Order.OrderStatus.PENDING);
                    break;
                case PREPARING:
                    order.setStatus(Order.OrderStatus.PREPARING);
                    break;
                case READY:
                    order.setStatus(Order.OrderStatus.READY);
                    break;
                case DELIVERED:
                    order.setStatus(Order.OrderStatus.DELIVERED);
                    break;
            }

            orderRepository.save(order);
            return true;
        }

        return false;
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUser().getUserId());

        switch (order.getStatus()) {
            case PENDING:
                dto.setStatus(OrderDTO.StatusEnum.PENDING);
                break;
            case PREPARING:
                dto.setStatus(OrderDTO.StatusEnum.PREPARING);
                break;
            case READY:
                dto.setStatus(OrderDTO.StatusEnum.READY);
                break;
            case DELIVERED:
                dto.setStatus(OrderDTO.StatusEnum.DELIVERED);
                break;
        }

        switch (order.getPaymentStatus()) {
            case PAID:
                dto.setPaymentStatus(OrderDTO.PaymentStatusEnum.PAID);
                break;
            case UNPAID:
                dto.setPaymentStatus(OrderDTO.PaymentStatusEnum.UNPAID);
                break;
            case REFUNDED:
                dto.setPaymentStatus(OrderDTO.PaymentStatusEnum.REFUNDED);
                break;
        }

        dto.setTotalPrice(order.getTotalPrice());

        List<CartItemDTO> items = new ArrayList<>();
        for (OrderItem orderItem : order.getItems()) {
            CartItemDTO itemDTO = new CartItemDTO();
            itemDTO.setItemId(orderItem.getMenuItem().getItemId());
            itemDTO.setQuantity(orderItem.getQuantity());
            itemDTO.setTotalPrice(orderItem.getTotalPrice());
            items.add(itemDTO);
        }

        dto.setItems(items);

        return dto;
    }
}