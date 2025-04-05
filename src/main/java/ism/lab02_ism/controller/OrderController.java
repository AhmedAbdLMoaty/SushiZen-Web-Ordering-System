package ism.lab02_ism.controller;

import ism.lab02_ism.api.OrdersApi;
import ism.lab02_ism.model.OrderDTO;
import ism.lab02_ism.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController implements OrdersApi {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ResponseEntity<Void> placeOrder(OrderDTO orderDTO) {
        if (orderDTO.getUserId() == null || orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            OrderDTO createdOrder = orderService.placeOrder(orderDTO);
            if (createdOrder != null) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<OrderDTO> getOrder(String orderId) {
        if (orderId == null || orderId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        OrderDTO order = orderService.getOrderById(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        try {
            Map<String, OrderDTO> ordersMap = orderService.getAllOrders();
            if (ordersMap != null && !ordersMap.isEmpty()) {
                List<OrderDTO> orders = new ArrayList<>(ordersMap.values());
                return ResponseEntity.ok(orders);
            }
            return ResponseEntity.ok(new ArrayList<>()); // Return empty list instead of 404
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}