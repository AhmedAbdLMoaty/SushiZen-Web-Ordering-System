package ism.lab02_ism.controller;

import ism.lab02_ism.api.KitchenApi;
import ism.lab02_ism.model.OrderDTO;
import ism.lab02_ism.model.OrderStatusDTO;
import ism.lab02_ism.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class KitchenOrderController implements KitchenApi {

    private final OrderService orderService;

    @Autowired
    public KitchenOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ResponseEntity<Void> updateOrderStatus(String orderId, OrderStatusDTO statusDTO) {
        if (orderId == null || orderId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (statusDTO == null || statusDTO.getStatus() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            OrderDTO.StatusEnum newStatus = OrderDTO.StatusEnum.fromValue(statusDTO.getStatus().getValue());
            boolean updated = orderService.updateOrderStatus(orderId, newStatus);

            if (updated) {
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/kitchen/orders")
    public ResponseEntity<Map<String, OrderDTO>> getAllOrders() {
        try {
            Map<String, OrderDTO> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}