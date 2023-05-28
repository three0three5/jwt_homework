package com.restaurant.order_service.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class OrderRequestDto {
    private List<DishRequestDto> dishRequestDtoList;
    private String specialRequests;
}
