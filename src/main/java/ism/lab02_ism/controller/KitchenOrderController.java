package ism.lab02_ism.controller;

import ism.lab02_ism.api.KitchenApi;
import ism.lab02_ism.model.OrderDTO;
import ism.lab02_ism.model.OrderStatusDTO;
import ism.lab02_ism.service.OrderService;
import ism.lab02_ism.util.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class KitchenOrderController implements KitchenApi {

    private static final Logger logger = LoggerFactory.getLogger(KitchenOrderController.class);

    private final OrderService orderService;
    private final AuthUtils authUtils;

    @Autowired
    public KitchenOrderController(OrderService orderService, AuthUtils authUtils) {
        this.orderService = orderService;
        this.authUtils = authUtils;
    }

    @Override
    public ResponseEntity<Void> updateOrderStatus(String orderId, OrderStatusDTO statusDTO) {
        // Remove authentication check
        // if (!authUtils.isKitchenStaff() && !authUtils.isAdmin()) {
        // return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        // }

        if (orderId == null || orderId.isEmpty()) {
            logger.warn("Invalid order ID received");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (statusDTO == null || statusDTO.getStatus() == null) {
            logger.warn("Invalid order status received");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            logger.debug("Updating status of order: {} to: {}", orderId, statusDTO.getStatus());
            OrderDTO.StatusEnum newStatus = OrderDTO.StatusEnum.fromValue(statusDTO.getStatus().getValue());
            boolean updated = orderService.updateOrderStatus(orderId, newStatus);

            if (updated) {
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                logger.warn("Order not found for status update: {}", orderId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid status value received", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Error updating order status for order: {}", orderId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/kitchen/orders")
    public ResponseEntity<Map<String, OrderDTO>> getAllOrders() {
        // Remove authentication check
        // if (!authUtils.isKitchenStaff() && !authUtils.isAdmin()) {
        // return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        // }

        try {
            logger.debug("Fetching all orders for kitchen");
            Map<String, OrderDTO> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("Error retrieving orders for kitchen", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}