package ism.lab02_ism.aspect;

import ism.lab02_ism.model.MenuItemDTO;
import ism.lab02_ism.model.OrderDTO;
import ism.lab02_ism.service.ApiStatisticsService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class ApiStatisticsAspect {

    private static final Logger logger = LoggerFactory.getLogger(ApiStatisticsAspect.class);

    private final ApiStatisticsService statisticsService;

    public ApiStatisticsAspect(ApiStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * Intercepts all controller API methods to collect statistics
     */
    @Around("execution(* ism.lab02_ism.controller.*Controller.*(..))")
    public Object trackApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        String endpoint = className + "." + methodName;

        // Record API call
        statisticsService.recordApiCall(endpoint);
        logger.debug("API call intercepted: {}", endpoint);

        // Measure response time
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        // Record response time
        statisticsService.recordResponseTime(endpoint, responseTime);
        logger.debug("API call to {} completed in {}ms", endpoint, responseTime);

        // Extract meaningful data from the response
        processResponse(result);

        return result;
    }

    /**
     * Process the API response to collect meaningful statistics
     */
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
}