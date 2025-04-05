package ism.lab02_ism.service;

import ism.lab02_ism.entity.*;
import ism.lab02_ism.model.CartItemDTO;
import ism.lab02_ism.model.OrderDTO;
import ism.lab02_ism.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

            java.math.BigDecimal totalPrice = java.math.BigDecimal.ZERO;

            for (CartItemDTO itemDTO : orderDTO.getItems()) {
                Optional<MenuItem> menuItemOptional = menuItemRepository.findByItemId(itemDTO.getItemId());

                if (menuItemOptional.isPresent()) {
                    MenuItem menuItem = menuItemOptional.get();

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setMenuItem(menuItem);
                    orderItem.setQuantity(itemDTO.getQuantity());

                    java.math.BigDecimal itemTotalPrice = new java.math.BigDecimal(
                            Float.toString(menuItem.getItemPrice() * itemDTO.getQuantity()));
                    orderItem.setTotalPrice(itemTotalPrice);

                    orderItemRepository.save(orderItem);
                    order.getItems().add(orderItem);

                    totalPrice = totalPrice.add(orderItem.getTotalPrice());
                }
            }

            order.setTotalPrice(totalPrice.floatValue());

            Optional<Cart> cartOptional = cartRepository.findByUser(user);
            if (cartOptional.isPresent()) {
                Cart cart = cartOptional.get();
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
        return orderRepository.findAll().stream()
                .collect(Collectors.toMap(Order::getOrderId, this::convertToDTO));
    }

    @Transactional
    public boolean updateOrderStatus(String orderId, OrderDTO.StatusEnum statusEnum) {
        Optional<Order> orderOptional = orderRepository.findByOrderId(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            Order.OrderStatus newStatus;
            switch (statusEnum) {
                case PENDING:
                    newStatus = Order.OrderStatus.PENDING;
                    break;
                case PREPARING:
                    newStatus = Order.OrderStatus.PREPARING;
                    break;
                case READY:
                    newStatus = Order.OrderStatus.READY;
                    break;
                case DELIVERED:
                    newStatus = Order.OrderStatus.DELIVERED;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown order status: " + statusEnum);
            }

            order.setStatus(newStatus);
            orderRepository.save(order);
            return true;
        }

        return false;
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUser().getUserId());
        dto.setTotalPrice(order.getTotalPrice());

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

        List<CartItemDTO> itemDTOs = order.getItems().stream()
                .map(this::convertToCartItemDTO)
                .collect(Collectors.toList());

        dto.setItems(itemDTOs);

        return dto;
    }

    private CartItemDTO convertToCartItemDTO(OrderItem orderItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setItemId(orderItem.getMenuItem().getItemId());
        dto.setQuantity(orderItem.getQuantity());

        dto.setTotalPrice(orderItem.getTotalPrice());
        return dto;
    }
}