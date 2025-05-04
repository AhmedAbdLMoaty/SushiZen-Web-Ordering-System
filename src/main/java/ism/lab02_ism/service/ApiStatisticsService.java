package ism.lab02_ism.service;

import ism.lab02_ism.model.ApiStatisticsDTO;
import ism.lab02_ism.model.MenuItemDTO;
import ism.lab02_ism.model.OrderDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ApiStatisticsService {

    // API call counters
    private final AtomicInteger totalApiCalls = new AtomicInteger(0);
    private final Map<String, AtomicInteger> endpointCallCounts = new ConcurrentHashMap<>();

    // Performance tracking
    private final Map<String, List<Long>> responseTimesByEndpoint = new ConcurrentHashMap<>();
    private final Map<String, Double> averageResponseTimes = new ConcurrentHashMap<>();

    // Menu item statistics
    private final AtomicReference<MenuItemDTO> highestPriceMenuItem = new AtomicReference<>(null);

    // Order statistics
    private final AtomicReference<OrderDTO> largestOrder = new AtomicReference<>(null);
    private final Map<String, AtomicInteger> itemOrderCounts = new ConcurrentHashMap<>();
    private final Map<String, String> itemIdToNameMap = new ConcurrentHashMap<>();

    /**
     * Records an API call
     * 
     * @param endpoint the endpoint being called
     */
    public void recordApiCall(String endpoint) {
        totalApiCalls.incrementAndGet();
        endpointCallCounts.computeIfAbsent(endpoint, k -> new AtomicInteger(0)).incrementAndGet();
    }

    /**
     * Records response time for an endpoint
     * 
     * @param endpoint the endpoint
     * @param timeMs   the response time in milliseconds
     */
    public void recordResponseTime(String endpoint, long timeMs) {
        responseTimesByEndpoint.computeIfAbsent(endpoint, k -> new java.util.ArrayList<>()).add(timeMs);

        // Update average
        List<Long> times = responseTimesByEndpoint.get(endpoint);
        double avg = times.stream().mapToLong(t -> t).average().orElse(0);
        averageResponseTimes.put(endpoint, Math.round(avg * 100) / 100.0);
    }

    /**
     * Processes menu items to find the highest price item
     * 
     * @param menuItems list of menu items
     */
    public void processMenuItems(List<MenuItemDTO> menuItems) {
        if (menuItems != null && !menuItems.isEmpty()) {
            for (MenuItemDTO item : menuItems) {
                processMenuItem(item);
            }
        }
    }

    /**
     * Process a single menu item
     * 
     * @param menuItem the menu item
     */
    public void processMenuItem(MenuItemDTO menuItem) {
        if (menuItem != null && menuItem.getItemPrice() != null && menuItem.getItemName() != null) {
            // Store item name for reference
            itemIdToNameMap.put(menuItem.getItemId(), menuItem.getItemName());

            // Check if this is the highest price item
            MenuItemDTO currentHighest = highestPriceMenuItem.get();
            if (currentHighest == null ||
                    menuItem.getItemPrice().compareTo(currentHighest.getItemPrice()) > 0) {
                highestPriceMenuItem.set(menuItem);
            }
        }
    }

    /**
     * Process an order for statistics
     * 
     * @param order the order
     */
    public void processOrder(OrderDTO order) {
        if (order == null || order.getTotalPrice() == null) {
            return;
        }

        // Check if this is the largest order
        OrderDTO currentLargest = largestOrder.get();
        if (currentLargest == null || order.getTotalPrice().compareTo(currentLargest.getTotalPrice()) > 0) {
            largestOrder.set(order);
        }

        // Track item popularity
        if (order.getItems() != null) {
            order.getItems().forEach(item -> {
                if (item.getItemId() != null && item.getQuantity() != null) {
                    itemOrderCounts.computeIfAbsent(item.getItemId(), k -> new AtomicInteger(0))
                            .addAndGet(item.getQuantity());
                }
            });
        }
    }

    /**
     * Gets the total number of API calls
     * 
     * @return total calls
     */
    public int getTotalCalls() {
        return totalApiCalls.get();
    }

    /**
     * Gets the call counts per endpoint
     * 
     * @return map of endpoint to call count
     */
    public Map<String, Integer> getEndpointCalls() {
        Map<String, Integer> result = new HashMap<>();
        endpointCallCounts.forEach((key, value) -> result.put(key, value.get()));
        return result;
    }

    /**
     * Gets the call percentages per endpoint
     * 
     * @return map of endpoint to percentage
     */
    public Map<String, Double> getEndpointCallPercentages() {
        Map<String, Double> percentages = new HashMap<>();

        int total = totalApiCalls.get();
        if (total > 0) {
            endpointCallCounts.forEach((endpoint, count) -> {
                double percentage = (count.get() * 100.0) / total;
                percentages.put(endpoint, Math.round(percentage * 100) / 100.0);
            });
        }

        return percentages;
    }

    /**
     * Gets the average response times per endpoint
     * 
     * @return map of endpoint to average response time
     */
    public Map<String, Double> getAverageResponseTimes() {
        return new HashMap<>(averageResponseTimes);
    }

    /**
     * Gets the current API statistics
     * 
     * @return API statistics
     */
    public ApiStatisticsDTO getStatistics() {
        ApiStatisticsDTO stats = new ApiStatisticsDTO();

        // API call statistics
        stats.setTotalApiCalls(totalApiCalls.get());

        Map<String, Integer> callCounts = new HashMap<>();
        Map<String, Double> callPercentages = new HashMap<>();

        double total = totalApiCalls.get();
        endpointCallCounts.forEach((endpoint, count) -> {
            int countValue = count.get();
            callCounts.put(endpoint, countValue);

            double percentage = 0;
            if (total > 0) {
                percentage = (countValue / total) * 100.0;
            }
            callPercentages.put(endpoint, Math.round(percentage * 100) / 100.0);
        });

        stats.setEndpointCalls(callCounts);
        stats.setEndpointCallPercentages(callPercentages);

        // Response time statistics
        stats.setAverageResponseTimes(new HashMap<>(averageResponseTimes));

        // Menu item statistics
        MenuItemDTO highestItem = highestPriceMenuItem.get();
        if (highestItem != null) {
            stats.setHighestPriceMenuItem(highestItem);
            stats.setHighestItemPrice(BigDecimal.valueOf(highestItem.getItemPrice()));
        }

        // Order statistics
        OrderDTO largest = largestOrder.get();
        if (largest != null) {
            stats.setLargestOrderId(largest.getOrderId());
            stats.setLargestOrderValue(largest.getTotalPrice());
        }

        // Most popular item
        String mostPopularItemId = null;
        int highestCount = 0;

        for (Map.Entry<String, AtomicInteger> entry : itemOrderCounts.entrySet()) {
            int count = entry.getValue().get();
            if (count > highestCount) {
                highestCount = count;
                mostPopularItemId = entry.getKey();
            }
        }

        if (mostPopularItemId != null) {
            stats.setMostPopularItemId(mostPopularItemId);
            stats.setMostPopularItemName(itemIdToNameMap.getOrDefault(mostPopularItemId, "Unknown Item"));
            stats.setMostPopularItemOrderCount(highestCount);
        }

        stats.setLastUpdated(LocalDateTime.now());

        return stats;
    }
}