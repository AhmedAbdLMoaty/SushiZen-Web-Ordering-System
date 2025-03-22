package ism.lab02_ism.controller;

import ism.lab02_ism.api.OrdersApi;
import ism.lab02_ism.model.OrderDTO;
import ism.lab02_ism.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController implements OrdersApi {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ResponseEntity<Void> placeOrder(OrderDTO orderDTO) {
        orderService.addOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<OrderDTO> getOrder(String orderId) {
        OrderDTO order = orderService.getOrder(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}