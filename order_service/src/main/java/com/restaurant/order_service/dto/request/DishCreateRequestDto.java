package com.restaurant.order_service.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class DishCreateRequestDto {
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
}
