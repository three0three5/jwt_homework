package com.restaurant.order_service.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class DishResponseDto {
    private String name;
    private String description;
    private Double price;
}
