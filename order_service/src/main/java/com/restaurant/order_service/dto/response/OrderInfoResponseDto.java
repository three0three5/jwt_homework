package com.restaurant.order_service.dto.response;

import com.restaurant.order_service.domain.OrderStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class OrderInfoResponseDto {
    private Integer orderId;
    List<DishIdDto> dishList;
    private String username;
    private String specialRequests;
    private OrderStatus status;
}
