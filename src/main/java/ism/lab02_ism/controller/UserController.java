package ism.lab02_ism.controller;

import ism.lab02_ism.api.UsersApi;
import ism.lab02_ism.model.LoginDTO;
import ism.lab02_ism.model.UserDTO;
import ism.lab02_ism.service.UserService;
import ism.lab02_ism.util.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController implements UsersApi {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final AuthUtils authUtils;

    @Autowired
    public UserController(UserService userService, AuthUtils authUtils) {
        this.userService = userService;
        this.authUtils = authUtils;
    }

    @Override
    public ResponseEntity<List<UserDTO>> getUsers() {
        if (!authUtils.isAdmin()) {
            logger.warn("Unauthorized attempt to get all users");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            logger.debug("Fetching all users");
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error retrieving all users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<UserDTO> getUserProfile(String userId) {
        String currentUserId = authUtils.getCurrentUserId();

        // User must be logged in
        if (currentUserId == null) {
            logger.warn("Unauthorized access attempt to get user profile");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Only allow users to access their own profile unless they're an admin
        if (!userId.equals(currentUserId) && !authUtils.isAdmin()) {
            logger.warn("User {} attempted to access profile of user {}", currentUserId, userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            logger.debug("Fetching user profile: {}", userId);
            UserDTO userDTO = userService.getUserById(userId);
            if (userDTO != null) {
                return ResponseEntity.ok(userDTO);
            } else {
                logger.warn("User not found: {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            logger.error("Error retrieving user profile: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<UserDTO> loginUser(LoginDTO loginDTO) {
        if (loginDTO == null || loginDTO.getEmail() == null || loginDTO.getPassword() == null) {
            logger.warn("Invalid login data received");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            logger.debug("Attempting login for user: {}", loginDTO.getEmail());
            UserDTO authenticatedUser = userService.authenticateUser(loginDTO);
            if (authenticatedUser != null) {
                return ResponseEntity.ok(authenticatedUser);
            } else {
                logger.warn("Authentication failed for user: {}", loginDTO.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            logger.error("Error during user authentication", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> registerUser(UserDTO userDTO) {
        if (userDTO == null || userDTO.getEmail() == null || userDTO.getPassword() == null ||
                userDTO.getName() == null || userDTO.getPhoneNumber() == null) {
            logger.warn("Invalid registration data received");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            logger.debug("Registering new user: {}", userDTO.getEmail());
            UserDTO registeredUser = userService.registerUser(userDTO);
            if (registeredUser != null) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                logger.warn("Registration failed - email already exists: {}", userDTO.getEmail());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } catch (Exception e) {
            logger.error("Error during user registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> updateUserProfile(String userId, UserDTO userDTO) {
        String currentUserId = authUtils.getCurrentUserId();

        // User must be logged in
        if (currentUserId == null) {
            logger.warn("Unauthorized access attempt to update user profile");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Only allow users to update their own profile unless they're an admin
        if (!userId.equals(currentUserId) && !authUtils.isAdmin()) {
            logger.warn("User {} attempted to update profile of user {}", currentUserId, userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (userDTO == null) {
            logger.warn("Invalid profile update data received");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            logger.debug("Updating profile for user: {}", userId);
            UserDTO updatedUser = userService.updateUser(userId, userDTO);
            if (updatedUser != null) {
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                logger.warn("User not found for update: {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            logger.error("Error updating user profile: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}