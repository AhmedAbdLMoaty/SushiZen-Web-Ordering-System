# UsersApi

All URIs are relative to *https://api.sushizen.com/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getUserProfile**](UsersApi.md#getUserProfile) | **GET** /users/{userId} | Get a user&#39;s profile |
| [**getUsers**](UsersApi.md#getUsers) | **GET** /users | Get all users |
| [**loginUser**](UsersApi.md#loginUser) | **POST** /users/login | User login authentication |
| [**registerUser**](UsersApi.md#registerUser) | **POST** /users/register | Register a new user |
| [**updateUserProfile**](UsersApi.md#updateUserProfile) | **PUT** /users/{userId} | Update a user&#39;s profile |


<a id="getUserProfile"></a>
# **getUserProfile**
> UserDTO getUserProfile(userId)

Get a user&#39;s profile

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UsersApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.sushizen.com/v1");

    UsersApi apiInstance = new UsersApi(defaultClient);
    String userId = "userId_example"; // String | 
    try {
      UserDTO result = apiInstance.getUserProfile(userId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UsersApi#getUserProfile");
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
| **userId** | **String**|  | |

### Return type

[**UserDTO**](UserDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | User profile data |  -  |

<a id="getUsers"></a>
# **getUsers**
> List&lt;UserDTO&gt; getUsers()

Get all users

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UsersApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.sushizen.com/v1");

    UsersApi apiInstance = new UsersApi(defaultClient);
    try {
      List<UserDTO> result = apiInstance.getUsers();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UsersApi#getUsers");
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

[**List&lt;UserDTO&gt;**](UserDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Successful operation |  -  |

<a id="loginUser"></a>
# **loginUser**
> UserDTO loginUser(loginDTO)

User login authentication

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UsersApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.sushizen.com/v1");

    UsersApi apiInstance = new UsersApi(defaultClient);
    LoginDTO loginDTO = new LoginDTO(); // LoginDTO | 
    try {
      UserDTO result = apiInstance.loginUser(loginDTO);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UsersApi#loginUser");
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
| **loginDTO** | [**LoginDTO**](LoginDTO.md)|  | |

### Return type

[**UserDTO**](UserDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Successful login |  -  |

<a id="registerUser"></a>
# **registerUser**
> registerUser(userDTO)

Register a new user

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UsersApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.sushizen.com/v1");

    UsersApi apiInstance = new UsersApi(defaultClient);
    UserDTO userDTO = new UserDTO(); // UserDTO | 
    try {
      apiInstance.registerUser(userDTO);
    } catch (ApiException e) {
      System.err.println("Exception when calling UsersApi#registerUser");
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
| **userDTO** | [**UserDTO**](UserDTO.md)|  | |

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
| **201** | User created successfully |  -  |

<a id="updateUserProfile"></a>
# **updateUserProfile**
> updateUserProfile(userId, userDTO)

Update a user&#39;s profile

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UsersApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.sushizen.com/v1");

    UsersApi apiInstance = new UsersApi(defaultClient);
    String userId = "userId_example"; // String | 
    UserDTO userDTO = new UserDTO(); // UserDTO | 
    try {
      apiInstance.updateUserProfile(userId, userDTO);
    } catch (ApiException e) {
      System.err.println("Exception when calling UsersApi#updateUserProfile");
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
| **userId** | **String**|  | |
| **userDTO** | [**UserDTO**](UserDTO.md)|  | |

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
| **200** | Profile updated successfully |  -  |

