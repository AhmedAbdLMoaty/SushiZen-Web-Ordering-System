package ism.lab02_ism.controller;

import ism.lab02_ism.api.KitchenApi;
import ism.lab02_ism.model.OrderDTO;
import ism.lab02_ism.model.OrderStatusDTO;
import ism.lab02_ism.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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
        
        OrderStatusDTO.StatusEnum statusEnum = statusDTO.getStatus();
        String statusValue = statusEnum.getValue(); 
        
        
        OrderDTO.StatusEnum newStatus = OrderDTO.StatusEnum.fromValue(statusValue);
        
        boolean updated = orderService.updateOrderStatus(orderId, newStatus);
        
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/kitchen/orders")
    public ResponseEntity<Map<String, OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
