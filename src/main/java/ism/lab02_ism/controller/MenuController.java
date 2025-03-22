package ism.lab02_ism.controller;

import ism.lab02_ism.api.MenuApi;
import ism.lab02_ism.model.MenuItemDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MenuController implements MenuApi {

    private final Map<String, MenuItemDTO> menuItems = new HashMap<>();

    public MenuController() {
        
        addMenuItem("1", "California Roll", "Crab and avocado roll", 8.99f, true);
        addMenuItem("2", "Salmon Nigiri", "Fresh salmon over rice", 6.99f, true);
    }

    private void addMenuItem(String id, String name, String description, float price, boolean available) {
        MenuItemDTO item = new MenuItemDTO();
        item.setItemId(id);
        item.setItemName(name);
        item.setItemDescription(description);
        item.setItemPrice(price);
        item.setAvailable(available);
        menuItems.put(id, item);
    }

    @Override
    public ResponseEntity<List<MenuItemDTO>> getMenuItems() {
        return ResponseEntity.ok(new ArrayList<>(menuItems.values()));
    }

    @Override
    public ResponseEntity<MenuItemDTO> getMenuItem(String itemId) {
        MenuItemDTO item = menuItems.get(itemId);
        if (item != null) {
            return ResponseEntity.ok(item);
        }
        return ResponseEntity.notFound().build();
    }
}