package ism.lab02_ism.controller;

import ism.lab02_ism.api.OrdersApi;
import ism.lab02_ism.model.OrderDTO;
import ism.lab02_ism.service.OrderService;
import ism.lab02_ism.util.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController implements OrdersApi {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final AuthUtils authUtils;

    @Autowired
    public OrderController(OrderService orderService, AuthUtils authUtils) {
        this.orderService = orderService;
        this.authUtils = authUtils;
    }

    @Override
    public ResponseEntity<Void> placeOrder(OrderDTO orderDTO) {
        // Use a default/guest user ID for testing if needed
        String userId = "guest-user";

        if (orderDTO == null || orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
            logger.warn("Invalid order data received");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Set the user ID
        orderDTO.setUserId(userId);

        try {
            logger.debug("Placing order for user: {}", userId);
            OrderDTO createdOrder = orderService.placeOrder(orderDTO);
            if (createdOrder != null) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        } catch (Exception e) {
            logger.error("Error placing order for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<OrderDTO> getOrder(String orderId) {
        if (orderId == null || orderId.isEmpty()) {
            logger.warn("Invalid order ID received");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            logger.debug("Fetching order: {}", orderId);
            OrderDTO order = orderService.getOrderById(orderId);

            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(order);
        } catch (Exception e) {
            logger.error("Error retrieving order: {}", orderId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        try {
            logger.debug("Fetching all orders");
            Map<String, OrderDTO> ordersMap = orderService.getAllOrders();
            if (ordersMap != null) {
                List<OrderDTO> orders = new ArrayList<>(ordersMap.values());
                return ResponseEntity.ok(orders);
            }
            return ResponseEntity.ok(new ArrayList<>());
        } catch (Exception e) {
            logger.error("Error retrieving all orders", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}