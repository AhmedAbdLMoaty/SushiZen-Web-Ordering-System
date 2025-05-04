package ism.lab02_ism.controller;

import ism.lab02_ism.api.MenuApi;
import ism.lab02_ism.model.MenuItemDTO;
import ism.lab02_ism.service.MenuService;
import ism.lab02_ism.util.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MenuController implements MenuApi {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    private final MenuService menuService;
    private final AuthUtils authUtils;

    @Autowired
    public MenuController(MenuService menuService, AuthUtils authUtils) {
        this.menuService = menuService;
        this.authUtils = authUtils;
    }

    @Override
    public ResponseEntity<List<MenuItemDTO>> getMenuItems() {
        try {
            logger.debug("Fetching all menu items");
            List<MenuItemDTO> menuItems = menuService.getAllMenuItems();
            return ResponseEntity.ok(menuItems);
        } catch (Exception e) {
            logger.error("Error retrieving menu items", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<MenuItemDTO> getMenuItem(String itemId) {
        if (itemId == null || itemId.isEmpty()) {
            logger.warn("Invalid item ID received");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            logger.debug("Fetching menu item: {}", itemId);
            MenuItemDTO item = menuService.getMenuItemById(itemId);
            if (item != null) {
                return ResponseEntity.ok(item);
            }
            logger.warn("Menu item not found: {}", itemId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error retrieving menu item: {}", itemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<MenuItemDTO> createMenuItem(MenuItemDTO menuItemDTO) {
        if (!authUtils.isAdmin()) {
            logger.warn("Unauthorized attempt to create menu item");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (menuItemDTO == null || menuItemDTO.getItemName() == null || menuItemDTO.getItemPrice() == null) {
            logger.warn("Invalid menu item data received");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            logger.debug("Creating new menu item: {}", menuItemDTO.getItemName());
            MenuItemDTO createdItem = menuService.createMenuItem(menuItemDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        } catch (Exception e) {
            logger.error("Error creating menu item", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<MenuItemDTO> updateMenuItem(String itemId, MenuItemDTO menuItemDTO) {
        if (!authUtils.isAdmin()) {
            logger.warn("Unauthorized attempt to update menu item");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (itemId == null || itemId.isEmpty() || menuItemDTO == null) {
            logger.warn("Invalid menu item update data received");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            logger.debug("Updating menu item: {}", itemId);
            MenuItemDTO updatedItem = menuService.updateMenuItem(itemId, menuItemDTO);
            if (updatedItem != null) {
                return ResponseEntity.ok(updatedItem);
            }
            logger.warn("Menu item not found for update: {}", itemId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error updating menu item: {}", itemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}