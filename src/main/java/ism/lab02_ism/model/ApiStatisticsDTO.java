package ism.lab02_ism.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class ApiStatisticsDTO {
    private int totalApiCalls;
    private Map<String, Integer> endpointCalls;
    private Map<String, Double> endpointCallPercentages;

    private MenuItemDTO highestPriceMenuItem;
    private BigDecimal highestItemPrice;

    private String mostPopularItemId;
    private String mostPopularItemName;
    private Integer mostPopularItemOrderCount;

    private String largestOrderId;
    private BigDecimal largestOrderValue;

    private Map<String, Double> averageResponseTimes;
    private LocalDateTime lastUpdated;

    // Getters and setters
    public int getTotalApiCalls() {
        return totalApiCalls;
    }

    public void setTotalApiCalls(int totalApiCalls) {
        this.totalApiCalls = totalApiCalls;
    }

    public Map<String, Integer> getEndpointCalls() {
        return endpointCalls;
    }

    public void setEndpointCalls(Map<String, Integer> endpointCalls) {
        this.endpointCalls = endpointCalls;
    }

    public Map<String, Double> getEndpointCallPercentages() {
        return endpointCallPercentages;
    }

    public void setEndpointCallPercentages(Map<String, Double> endpointCallPercentages) {
        this.endpointCallPercentages = endpointCallPercentages;
    }

    public MenuItemDTO getHighestPriceMenuItem() {
        return highestPriceMenuItem;
    }

    public void setHighestPriceMenuItem(MenuItemDTO highestPriceMenuItem) {
        this.highestPriceMenuItem = highestPriceMenuItem;
    }

    public BigDecimal getHighestItemPrice() {
        return highestItemPrice;
    }

    public void setHighestItemPrice(BigDecimal highestItemPrice) {
        this.highestItemPrice = highestItemPrice;
    }

    public String getMostPopularItemId() {
        return mostPopularItemId;
    }

    public void setMostPopularItemId(String mostPopularItemId) {
        this.mostPopularItemId = mostPopularItemId;
    }

    public String getMostPopularItemName() {
        return mostPopularItemName;
    }

    public void setMostPopularItemName(String mostPopularItemName) {
        this.mostPopularItemName = mostPopularItemName;
    }

    public Integer getMostPopularItemOrderCount() {
        return mostPopularItemOrderCount;
    }

    public void setMostPopularItemOrderCount(Integer mostPopularItemOrderCount) {
        this.mostPopularItemOrderCount = mostPopularItemOrderCount;
    }

    public String getLargestOrderId() {
        return largestOrderId;
    }

    public void setLargestOrderId(String largestOrderId) {
        this.largestOrderId = largestOrderId;
    }

    public BigDecimal getLargestOrderValue() {
        return largestOrderValue;
    }

    public void setLargestOrderValue(BigDecimal largestOrderValue) {
        this.largestOrderValue = largestOrderValue;
    }

    public Map<String, Double> getAverageResponseTimes() {
        return averageResponseTimes;
    }

    public void setAverageResponseTimes(Map<String, Double> averageResponseTimes) {
        this.averageResponseTimes = averageResponseTimes;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}