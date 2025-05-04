package ism.lab02_ism.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Utility class for handling user authentication and authorization
 */
@Component
public class AuthUtils {

    private static final Logger logger = LoggerFactory.getLogger(AuthUtils.class);
    private final HttpServletRequest request;

    public AuthUtils(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Gets the current user ID from request headers or defaults to a test user
     * 
     * @return The user ID
     */
    public String getCurrentUserId() {
        // Get user ID from header (for testing with different users)
        String userId = request.getHeader("X-User-ID");
        if (userId != null && !userId.isEmpty()) {
            logger.debug("Using user ID from header: {}", userId);
            return userId;
        }

        // Default for testing: return user "1"
        logger.debug("Using default user ID: 1");
        return "1";
    }

    /**
     * Check if the current user is an admin
     * 
     * @return true if the user is an admin
     */
    public boolean isAdmin() {
        String role = request.getHeader("X-User-Role");
        String userId = getCurrentUserId();

        return "ADMIN".equals(role) || "admin1".equals(userId);
    }

    /**
     * Check if the current user is kitchen staff
     * 
     * @return true if the user is kitchen staff
     */
    public boolean isKitchenStaff() {
        String role = request.getHeader("X-User-Role");
        String userId = getCurrentUserId();

        return "KITCHEN_STAFF".equals(role) || "kitchen1".equals(userId);
    }
}