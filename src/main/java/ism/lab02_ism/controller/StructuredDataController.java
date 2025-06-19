package ism.lab02_ism.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ism.lab02_ism.entity.MenuItem;
import ism.lab02_ism.entity.Restaurant;
import ism.lab02_ism.repository.MenuItemRepository;
import ism.lab02_ism.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/structured-data")
@CrossOrigin(origins = "*")
public class StructuredDataController {

  private final MenuItemRepository menuItemRepository;
  private final RestaurantRepository restaurantRepository;
  private final ObjectMapper objectMapper;

  @Autowired
  public StructuredDataController(
      MenuItemRepository menuItemRepository,
      RestaurantRepository restaurantRepository) {
    this.menuItemRepository = menuItemRepository;
    this.restaurantRepository = restaurantRepository;
    this.objectMapper = new ObjectMapper();
  }

  @GetMapping(value = "/menu-item/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getMenuItemStructuredData(@PathVariable String itemId) {
    // Get real menu item from database
    MenuItem menuItem = menuItemRepository.findByItemId(itemId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    ObjectNode jsonLd = objectMapper.createObjectNode();
    jsonLd.put("@context", "https://schema.org");
    jsonLd.put("@type", "MenuItem");
    jsonLd.put("@id", "https://sushizen.com/menu/item/" + menuItem.getItemId());
    jsonLd.put("name", menuItem.getItemName());
    jsonLd.put("description", menuItem.getItemDescription());
    jsonLd.put("image", menuItem.getItemPicture());

    ObjectNode offer = objectMapper.createObjectNode();
    offer.put("@type", "Offer");
    offer.put("price", String.format("%.2f", menuItem.getItemPrice()));
    offer.put("priceCurrency", "USD");
    offer.put("availability", menuItem.isAvailable() ? "InStock" : "OutOfStock");
    jsonLd.set("offers", offer);

    // Add any custom properties that exist
    if (menuItem.getJapaneseName() != null) {
      jsonLd.put("hasJapaneseName", menuItem.getJapaneseName());
    }

    return ResponseEntity.ok(jsonLd.toString());
  }

  @GetMapping(value = "/restaurant", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getRestaurantStructuredData() {
    // Get real restaurant data from database
    Restaurant restaurant = restaurantRepository.findById("main")
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    ObjectNode jsonLd = objectMapper.createObjectNode();
    jsonLd.put("@context", "https://schema.org");
    jsonLd.put("@type", "Restaurant");
    jsonLd.put("name", restaurant.getName());
    jsonLd.put("description", restaurant.getDescription());
    jsonLd.put("telephone", restaurant.getTelephone());
    jsonLd.put("priceRange", restaurant.getPriceRange());

    // Add address if available
    if (restaurant.getAddress() != null) {
      ObjectNode address = objectMapper.createObjectNode();
      address.put("@type", "PostalAddress");
      address.put("streetAddress", restaurant.getAddress().getStreetAddress());
      address.put("addressLocality", restaurant.getAddress().getAddressLocality());
      address.put("addressRegion", restaurant.getAddress().getAddressRegion());
      address.put("postalCode", restaurant.getAddress().getPostalCode());
      jsonLd.set("address", address);
    }

    return ResponseEntity.ok(jsonLd.toString());
  }

  @GetMapping(value = "/website", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getWebsiteStructuredData() {
    ObjectNode jsonLd = objectMapper.createObjectNode();
    jsonLd.put("@context", "https://schema.org");
    jsonLd.put("@type", "WebSite");
    jsonLd.put("name", "Sushi Zen");
    jsonLd.put("url", "https://sushizen.com");

    ObjectNode potentialAction = objectMapper.createObjectNode();
    potentialAction.put("@type", "SearchAction");
    potentialAction.put("target", "https://sushizen.com/search?q={search_term_string}");
    potentialAction.put("query-input", "required name=search_term_string");
    jsonLd.set("potentialAction", potentialAction);

    return ResponseEntity.ok(jsonLd.toString());
  }

  @PostMapping(value = "/breadcrumb", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getBreadcrumbStructuredData(@RequestBody List<Map<String, String>> breadcrumbs) {
    ObjectNode jsonLd = objectMapper.createObjectNode();
    jsonLd.put("@context", "https://schema.org");
    jsonLd.put("@type", "BreadcrumbList");

    ArrayNode itemListElement = objectMapper.createArrayNode();
    for (int i = 0; i < breadcrumbs.size(); i++) {
      Map<String, String> crumb = breadcrumbs.get(i);
      ObjectNode listItem = objectMapper.createObjectNode();
      listItem.put("@type", "ListItem");
      listItem.put("position", i + 1);
      listItem.put("name", crumb.get("name"));
      listItem.put("item", crumb.get("url"));
      itemListElement.add(listItem);
    }
    jsonLd.set("itemListElement", itemListElement);

    return ResponseEntity.ok(jsonLd.toString());
  }

  @GetMapping(value = "/menu", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getMenuStructuredData() {
    // Get all menu items from database
    List<MenuItem> menuItems = menuItemRepository.findAll();

    ObjectNode jsonLd = objectMapper.createObjectNode();
    jsonLd.put("@context", "https://schema.org");
    jsonLd.put("@type", "Menu");
    jsonLd.put("name", "Sushi Zen Menu"); // Group by categories (handle null categories gracefully)
    Map<String, List<MenuItem>> itemsByCategory = menuItems.stream()
        .collect(Collectors.groupingBy(item -> item.getCategory() != null ? item.getCategory() : "Other"));

    ArrayNode sections = objectMapper.createArrayNode();
    itemsByCategory.forEach((category, items) -> {
      ObjectNode section = objectMapper.createObjectNode();
      section.put("@type", "MenuSection");
      section.put("name", category);

      ArrayNode sectionItems = objectMapper.createArrayNode();
      items.forEach(item -> {
        ObjectNode itemNode = objectMapper.createObjectNode();
        itemNode.put("@type", "MenuItem");
        itemNode.put("name", item.getItemName());
        itemNode.put("description", item.getItemDescription());

        ObjectNode itemOffer = objectMapper.createObjectNode();
        itemOffer.put("@type", "Offer");
        itemOffer.put("price", String.format("%.2f", item.getItemPrice()));
        itemOffer.put("priceCurrency", "USD");
        sectionItems.add(itemNode);
      });

      section.set("hasMenuItem", sectionItems);
      sections.add(section);
    });

    jsonLd.set("hasMenuSection", sections);

    return ResponseEntity.ok(jsonLd.toString());
  }
}
