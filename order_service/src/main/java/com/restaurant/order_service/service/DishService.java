package com.restaurant.order_service.service;

import com.restaurant.order_service.domain.repository.DishRepository;
import com.restaurant.order_service.domain.repository.OrderRepository;
import com.restaurant.order_service.dto.request.DishCreateRequestDto;
import com.restaurant.order_service.dto.request.DishDeleteRequestDto;
import com.restaurant.order_service.dto.response.DishResponseDto;
import com.restaurant.order_service.service.exception.InvalidOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;

    public List<? extends DishResponseDto> getMenu(String token) throws InvalidOperationException {
        return null;
    }

    public String addDish(String token, DishCreateRequestDto dishRequestDto) throws InvalidOperationException {
        return null;
    }

    public String deleteDish(String token, DishDeleteRequestDto requestDto) throws InvalidOperationException {
        return null;
    }
}
