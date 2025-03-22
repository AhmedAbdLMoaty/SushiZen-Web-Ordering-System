# MenuApi

All URIs are relative to *https://api.sushizen.com/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getMenuItem**](MenuApi.md#getMenuItem) | **GET** /menu/{itemId} | Get details of a specific menu item |
| [**getMenuItems**](MenuApi.md#getMenuItems) | **GET** /menu | Get all menu items |


<a id="getMenuItem"></a>
# **getMenuItem**
> MenuItemDTO getMenuItem(itemId)

Get details of a specific menu item

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.MenuApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.sushizen.com/v1");

    MenuApi apiInstance = new MenuApi(defaultClient);
    String itemId = "itemId_example"; // String | 
    try {
      MenuItemDTO result = apiInstance.getMenuItem(itemId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling MenuApi#getMenuItem");
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

[**MenuItemDTO**](MenuItemDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Menu item details |  -  |

<a id="getMenuItems"></a>
# **getMenuItems**
> List&lt;MenuItemDTO&gt; getMenuItems()

Get all menu items

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.MenuApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.sushizen.com/v1");

    MenuApi apiInstance = new MenuApi(defaultClient);
    try {
      List<MenuItemDTO> result = apiInstance.getMenuItems();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling MenuApi#getMenuItems");
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

[**List&lt;MenuItemDTO&gt;**](MenuItemDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | List of menu items |  -  |

