package com.restaurant.order_service.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class OrderStatusRequestDto {
    private Integer orderId;
    private String status;
}
