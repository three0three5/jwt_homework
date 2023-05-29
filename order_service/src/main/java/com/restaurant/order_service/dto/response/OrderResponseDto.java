package com.restaurant.order_service.dto.response;

import com.restaurant.order_service.domain.OrderStatus;
import com.restaurant.order_service.domain.entity.OrderDish;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class OrderResponseDto {
    private Integer id;
    private String username;
    private OrderStatus status;
    private String specialRequests;
    private Timestamp updatedAt;
}
