package ism.lab02_ism.controller;

import ism.lab02_ism.api.MenuApi;
import ism.lab02_ism.model.MenuItemDTO;
import ism.lab02_ism.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MenuController implements MenuApi {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    public ResponseEntity<List<MenuItemDTO>> getMenuItems() {
        List<MenuItemDTO> menuItems = menuService.getAllMenuItems();
        return ResponseEntity.ok(menuItems);
    }

    @Override
    public ResponseEntity<MenuItemDTO> getMenuItem(String itemId) {
        if (itemId == null || itemId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        MenuItemDTO item = menuService.getMenuItemById(itemId);
        if (item != null) {
            return ResponseEntity.ok(item);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<MenuItemDTO> createMenuItem(MenuItemDTO menuItemDTO) {
        try {
            if (menuItemDTO.getItemName() == null || menuItemDTO.getItemPrice() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            MenuItemDTO createdItem = menuService.createMenuItem(menuItemDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<MenuItemDTO> updateMenuItem(String itemId, MenuItemDTO menuItemDTO) {
        try {
            if (itemId == null || itemId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            MenuItemDTO updatedItem = menuService.updateMenuItem(itemId, menuItemDTO);
            if (updatedItem != null) {
                return ResponseEntity.ok(updatedItem);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}