# DefaultApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**usersGet**](DefaultApi.md#usersGet) | **GET** /users |  |


<a id="usersGet"></a>
# **usersGet**
> List&lt;UsersGet200ResponseInner&gt; usersGet(limit, offset)



returns all users information

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP basic authorization: BasicAuth
    HttpBasicAuth BasicAuth = (HttpBasicAuth) defaultClient.getAuthentication("BasicAuth");
    BasicAuth.setUsername("YOUR USERNAME");
    BasicAuth.setPassword("YOUR PASSWORD");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    Integer limit = 56; // Integer | limits the number of users returned
    Integer offset = 56; // Integer | page number of the user query
    try {
      List<UsersGet200ResponseInner> result = apiInstance.usersGet(limit, offset);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#usersGet");
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
| **limit** | **Integer**| limits the number of users returned | [optional] |
| **offset** | **Integer**| page number of the user query | [optional] |

### Return type

[**List&lt;UsersGet200ResponseInner&gt;**](UsersGet200ResponseInner.md)

### Authorization

[BasicAuth](../README.md#BasicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Successfully returned list of all users. |  -  |
| **400** | Invalid request |  -  |

