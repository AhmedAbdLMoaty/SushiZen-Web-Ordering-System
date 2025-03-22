# OrdersApi

All URIs are relative to *https://api.sushizen.com/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getOrder**](OrdersApi.md#getOrder) | **GET** /orders/{orderId} | Get order details |
| [**placeOrder**](OrdersApi.md#placeOrder) | **POST** /orders | Place an order from the cart |


<a id="getOrder"></a>
# **getOrder**
> OrderDTO getOrder(orderId)

Get order details

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.OrdersApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.sushizen.com/v1");

    OrdersApi apiInstance = new OrdersApi(defaultClient);
    String orderId = "orderId_example"; // String | 
    try {
      OrderDTO result = apiInstance.getOrder(orderId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling OrdersApi#getOrder");
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
| **orderId** | **String**|  | |

### Return type

[**OrderDTO**](OrderDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Order details |  -  |

<a id="placeOrder"></a>
# **placeOrder**
> placeOrder(orderDTO)

Place an order from the cart

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.OrdersApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.sushizen.com/v1");

    OrdersApi apiInstance = new OrdersApi(defaultClient);
    OrderDTO orderDTO = new OrderDTO(); // OrderDTO | 
    try {
      apiInstance.placeOrder(orderDTO);
    } catch (ApiException e) {
      System.err.println("Exception when calling OrdersApi#placeOrder");
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
| **orderDTO** | [**OrderDTO**](OrderDTO.md)|  | |

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
| **201** | Order placed successfully |  -  |

