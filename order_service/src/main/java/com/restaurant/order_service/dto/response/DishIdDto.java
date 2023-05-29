package com.restaurant.order_service.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class DishIdDto {
    private Integer id;
    private Double price;
    private Integer quantity;
}
