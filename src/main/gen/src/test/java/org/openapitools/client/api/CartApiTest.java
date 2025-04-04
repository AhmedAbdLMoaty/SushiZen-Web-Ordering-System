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
import org.openapitools.client.model.CartDTO;
import org.openapitools.client.model.CartItemDTO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for CartApi
 */
@Disabled
public class CartApiTest {

    private final CartApi api = new CartApi();

    /**
     * Add an item to the cart
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void addItemToCartTest() throws ApiException {
        CartItemDTO cartItemDTO = null;
        api.addItemToCart(cartItemDTO);
        // TODO: test validations
    }

    /**
     * Retrieve user&#39;s cart
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getUserCartTest() throws ApiException {
        CartDTO response = api.getUserCart();
        // TODO: test validations
    }

    /**
     * Remove an item from the cart
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void removeItemFromCartTest() throws ApiException {
        String itemId = null;
        api.removeItemFromCart(itemId);
        // TODO: test validations
    }

}
