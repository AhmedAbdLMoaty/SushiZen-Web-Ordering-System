package ism.lab02_ism.controller;

import ism.lab02_ism.aspect.ConsoleStatisticsLogger;
import ism.lab02_ism.model.ApiStatisticsDTO;
import ism.lab02_ism.model.CartItemDTO;
import ism.lab02_ism.model.OrderDTO;
import ism.lab02_ism.model.MenuItemDTO;
import ism.lab02_ism.model.ApiStatisticsDTO;
import ism.lab02_ism.service.ApiStatisticsService;
import ism.lab02_ism.service.MenuService;
import ism.lab02_ism.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "*")
public class StatisticsController {

    private final ApiStatisticsService apiStats;
    private final MenuService menuService;
    private final OrderService orderService;

    // Additional statistics we want to track
    private final AtomicReference<MenuItemDTO> highestPriceItem = new AtomicReference<>();
    private final AtomicReference<OrderDTO> largestOrder = new AtomicReference<>();
    private final Map<String, Integer> itemOrderCounts = new HashMap<>();

    @Autowired
    public StatisticsController(ApiStatisticsService apiStats,
            MenuService menuService,
            OrderService orderService) {
        this.apiStats = apiStats;
        this.menuService = menuService;
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<ApiStatisticsDTO> getStatistics() {
        ApiStatisticsDTO stats = new ApiStatisticsDTO();

        // Get API usage statistics from the service
        stats.setTotalApiCalls(apiStats.getTotalCalls());
        stats.setEndpointCalls(apiStats.getEndpointCalls());
        stats.setEndpointCallPercentages(apiStats.getEndpointCallPercentages());
        stats.setAverageResponseTimes(apiStats.getAverageResponseTimes());

        // Calculate data statistics
        calculateDataStatistics(stats);

        // Add timestamp
        stats.setLastUpdated(LocalDateTime.now());

        return ResponseEntity.ok(stats);
    }

    private void calculateDataStatistics(ApiStatisticsDTO stats) {
        // Find highest price menu item
        List<MenuItemDTO> menuItems = menuService.getAllMenuItems();
        if (menuItems != null && !menuItems.isEmpty()) {
            MenuItemDTO highestPrice = menuItems.stream()
                    .max((a, b) -> Float.compare(a.getItemPrice(), b.getItemPrice()))
                    .orElse(null);

            if (highestPrice != null) {
                highestPriceItem.set(highestPrice);
                stats.setHighestPriceMenuItem(highestPrice);
                stats.setHighestItemPrice(BigDecimal.valueOf(highestPrice.getItemPrice()));
            }
        }

        // Find largest order
        Map<String, OrderDTO> orders = orderService.getAllOrders();
        if (orders != null && !orders.isEmpty()) {
            OrderDTO largest = orders.values().stream()
                    .max((a, b) -> a.getTotalPrice().compareTo(b.getTotalPrice()))
                    .orElse(null);

            if (largest != null) {
                largestOrder.set(largest);
                stats.setLargestOrderId(largest.getOrderId());
                stats.setLargestOrderValue(largest.getTotalPrice());
            }

            // Count ordered items
            for (OrderDTO order : orders.values()) {
                if (order.getItems() != null) {
                    for (CartItemDTO item : order.getItems()) {
                        itemOrderCounts.compute(item.getItemId(),
                                (k, v) -> v == null ? item.getQuantity() : v + item.getQuantity());
                    }
                }
            }

            // Find most popular item
            if (!itemOrderCounts.isEmpty()) {
                Map.Entry<String, Integer> mostPopular = itemOrderCounts.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .orElse(null);

                if (mostPopular != null) {
                    stats.setMostPopularItemId(mostPopular.getKey());
                    stats.setMostPopularItemOrderCount(mostPopular.getValue());

                    // Find item name
                    menuItems.stream()
                            .filter(item -> item.getItemId().equals(mostPopular.getKey()))
                            .findFirst()
                            .ifPresent(item -> stats.setMostPopularItemName(item.getItemName()));
                }
            }
        }
    }
}