

# OrderDTO


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**orderId** | **String** |  |  |
|**userId** | **String** |  |  |
|**items** | [**List&lt;CartItemDTO&gt;**](CartItemDTO.md) |  |  |
|**status** | [**StatusEnum**](#StatusEnum) |  |  |
|**totalPrice** | **BigDecimal** |  |  |
|**paymentStatus** | [**PaymentStatusEnum**](#PaymentStatusEnum) |  |  |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| PENDING | &quot;pending&quot; |
| PREPARING | &quot;preparing&quot; |
| READY | &quot;ready&quot; |
| DELIVERED | &quot;delivered&quot; |



## Enum: PaymentStatusEnum

| Name | Value |
|---- | -----|
| PAID | &quot;paid&quot; |
| UNPAID | &quot;unpaid&quot; |
| REFUNDED | &quot;refunded&quot; |



