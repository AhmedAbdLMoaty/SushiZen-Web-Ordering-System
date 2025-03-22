# CartApi

All URIs are relative to *https://api.sushizen.com/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**addItemToCart**](CartApi.md#addItemToCart) | **POST** /cart | Add an item to the cart |
| [**getUserCart**](CartApi.md#getUserCart) | **GET** /cart | Retrieve user&#39;s cart |
| [**removeItemFromCart**](CartApi.md#removeItemFromCart) | **DELETE** /cart/{itemId} | Remove an item from the cart |


<a id="addItemToCart"></a>
# **addItemToCart**
> addItemToCart(cartItemDTO)

Add an item to the cart

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CartApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.sushizen.com/v1");

    CartApi apiInstance = new CartApi(defaultClient);
    CartItemDTO cartItemDTO = new CartItemDTO(); // CartItemDTO | 
    try {
      apiInstance.addItemToCart(cartItemDTO);
    } catch (ApiException e) {
      System.err.println("Exception when calling CartApi#addItemToCart");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **cartItemDTO** | [**CartItemDTO**](CartItemDTO.md)|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Item added to cart |  -  |

<a id="getUserCart"></a>
# **getUserCart**
> CartDTO getUserCart()

Retrieve user&#39;s cart

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CartApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.sushizen.com/v1");

    CartApi apiInstance = new CartApi(defaultClient);
    try {
      CartDTO result = apiInstance.getUserCart();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CartApi#getUserCart");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**CartDTO**](CartDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Current cart items |  -  |

<a id="removeItemFromCart"></a>
# **removeItemFromCart**
> removeItemFromCart(itemId)

Remove an item from the cart

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CartApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.sushizen.com/v1");

    CartApi apiInstance = new CartApi(defaultClient);
    String itemId = "itemId_example"; // String | 
    try {
      apiInstance.removeItemFromCart(itemId);
    } catch (ApiException e) {
      System.err.println("Exception when calling CartApi#removeItemFromCart");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **itemId** | **String**|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Item removed successfully |  -  |

