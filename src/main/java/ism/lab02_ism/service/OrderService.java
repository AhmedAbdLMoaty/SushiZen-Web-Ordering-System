package ism.lab02_ism.service;

import ism.lab02_ism.model.OrderDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderService {

    private final Map<String, OrderDTO> orders = new HashMap<>();

    public Map<String, OrderDTO> getAllOrders() {
        return orders;
    }

    public OrderDTO getOrder(String orderId) {
        return orders.get(orderId);
    }

    public void addOrder(OrderDTO order) {
        if (order.getOrderId() == null) {
            order.setOrderId(UUID.randomUUID().toString());
        }
        
        if (order.getStatus() == null) {
            order.setStatus(OrderDTO.StatusEnum.PENDING);
        }
        
        if (order.getPaymentStatus() == null) {
            order.setPaymentStatus(OrderDTO.PaymentStatusEnum.UNPAID);
        }
        
        orders.put(order.getOrderId(), order);
    }

    public boolean updateOrderStatus(String orderId, OrderDTO.StatusEnum status) {
        OrderDTO order = orders.get(orderId);
        if (order == null) {
            return false;
        }
        order.setStatus(status);
        return true;
    }
}