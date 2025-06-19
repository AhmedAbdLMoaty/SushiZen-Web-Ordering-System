package ism.lab02_ism.service;

import ism.lab02_ism.entity.MenuItem;
import ism.lab02_ism.model.MenuItemDTO;
import ism.lab02_ism.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuItemRepository menuItemRepository;

    @Autowired
    public MenuService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuItemDTO> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MenuItemDTO getMenuItemById(String itemId) {
        Optional<MenuItem> menuItemOptional = menuItemRepository.findByItemId(itemId);
        return menuItemOptional.map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public MenuItemDTO createMenuItem(MenuItemDTO menuItemDTO) {
        MenuItem menuItem = convertToEntity(menuItemDTO);

        if (menuItem.getItemId() == null) {
            menuItem.setItemId(UUID.randomUUID().toString());
        }

        menuItem = menuItemRepository.save(menuItem);
        return convertToDTO(menuItem);
    }

    @Transactional
    public MenuItemDTO updateMenuItem(String itemId, MenuItemDTO menuItemDTO) {
        Optional<MenuItem> menuItemOptional = menuItemRepository.findByItemId(itemId);

        if (menuItemOptional.isPresent()) {
            MenuItem menuItem = menuItemOptional.get();

            menuItem.setItemName(menuItemDTO.getItemName());
            menuItem.setItemDescription(menuItemDTO.getItemDescription());
            menuItem.setItemPrice(menuItemDTO.getItemPrice());

            if (menuItemDTO.getItemPicture() != null) {
                menuItem.setItemPicture(menuItemDTO.getItemPicture().toString());
            }

            menuItem.setAvailable(menuItemDTO.getAvailable());

            menuItem = menuItemRepository.save(menuItem);
            return convertToDTO(menuItem);
        }

        return null;
    }

    private MenuItemDTO convertToDTO(MenuItem menuItem) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.setItemId(menuItem.getItemId());
        dto.setItemName(menuItem.getItemName());
        dto.setItemDescription(menuItem.getItemDescription());
        dto.setItemPrice(menuItem.getItemPrice());

        if (menuItem.getItemPicture() != null) {
            try {
                dto.setItemPicture(new java.net.URI(menuItem.getItemPicture()));
            } catch (java.net.URISyntaxException e) {
            }
        }

        dto.setAvailable(menuItem.isAvailable());
        return dto;
    }

    private MenuItem convertToEntity(MenuItemDTO dto) {
        MenuItem menuItem = new MenuItem();
        menuItem.setItemId(dto.getItemId());
        menuItem.setItemName(dto.getItemName());
        menuItem.setItemDescription(dto.getItemDescription());
        menuItem.setItemPrice(dto.getItemPrice());

        if (dto.getItemPicture() != null) {
            menuItem.setItemPicture(dto.getItemPicture().toString());
        }

        menuItem.setAvailable(dto.getAvailable());
        return menuItem;
    }
}