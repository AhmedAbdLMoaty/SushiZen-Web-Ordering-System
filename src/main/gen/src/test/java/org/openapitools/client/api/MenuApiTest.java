/*
 * SushiZen Web Ordering System APIs
 * Provides functionalities for users, menu items, cart, and order management.
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.api;

import org.openapitools.client.ApiException;
import org.openapitools.client.model.MenuItemDTO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for MenuApi
 */
@Disabled
public class MenuApiTest {

    private final MenuApi api = new MenuApi();

    /**
     * Get details of a specific menu item
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getMenuItemTest() throws ApiException {
        String itemId = null;
        MenuItemDTO response = api.getMenuItem(itemId);
        // TODO: test validations
    }

    /**
     * Get all menu items
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getMenuItemsTest() throws ApiException {
        List<MenuItemDTO> response = api.getMenuItems();
        // TODO: test validations
    }

}
