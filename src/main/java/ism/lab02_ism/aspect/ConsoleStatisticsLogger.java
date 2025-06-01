package ism.lab02_ism.aspect;

import ism.lab02_ism.model.MenuItemDTO;
import ism.lab02_ism.model.OrderDTO;
import ism.lab02_ism.service.ApiStatisticsService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class ConsoleStatisticsLogger {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleStatisticsLogger.class);

    private final ApiStatisticsService statisticsService;

    public ConsoleStatisticsLogger(ApiStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) || within(@org.springframework.stereotype.Controller *)")
    public void apiMethods() {
    }

    @Around("apiMethods()")
    public Object logApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String controllerName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String endpoint = controllerName + "." + methodName;

        if (controllerName.equals("StatisticsController")) {
            return joinPoint.proceed();
        }

        // Adding INFO level logging to clearly see aspect execution
        logger.info("AOP ASPECT TRIGGERED: API call to {}", endpoint);

        // Rest of your method remains the same
        statisticsService.recordApiCall(endpoint);

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        statisticsService.recordResponseTime(endpoint, responseTime);
        logger.info("API call to {} completed in {}ms", endpoint, responseTime);

        processResponse(result);

        int totalCalls = statisticsService.getTotalCalls();
        if (totalCalls % 5 == 0) {
            printStatistics();
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private void processResponse(Object result) {
        if (result instanceof ResponseEntity) {
            Object body = ((ResponseEntity<?>) result).getBody();

            if (body == null) {
                return;
            }

            // Process menu items
            if (body instanceof List && !((List<?>) body).isEmpty() && ((List<?>) body).get(0) instanceof MenuItemDTO) {
                statisticsService.processMenuItems((List<MenuItemDTO>) body);
                logger.debug("Processed menu items list for statistics");
            }
            // Process single menu item
            else if (body instanceof MenuItemDTO) {
                statisticsService.processMenuItem((MenuItemDTO) body);
                logger.debug("Processed menu item for statistics");
            }
            // Process order
            else if (body instanceof OrderDTO) {
                statisticsService.processOrder((OrderDTO) body);
                logger.debug("Processed order for statistics");
            }
        }
    }

    private void printStatistics() {
        StringBuilder sb = new StringBuilder("\n=== API STATISTICS ===\n");
        sb.append("Total API calls: ").append(statisticsService.getTotalCalls()).append("\n");
        sb.append("API usage breakdown:\n");

        Map<String, Integer> callCounts = statisticsService.getEndpointCalls();
        Map<String, Double> percentages = statisticsService.getEndpointCallPercentages();

        callCounts.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .forEach(entry -> {
                    String endpoint = entry.getKey();
                    int count = entry.getValue();
                    double percentage = percentages.getOrDefault(endpoint, 0.0);

                    sb.append(String.format("  %s: %d calls (%.1f%%)\n", endpoint, count, percentage));
                });

        logger.info(sb.toString());
    }
}