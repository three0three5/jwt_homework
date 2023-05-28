package com.restaurant.order_service.service;

import com.restaurant.order_service.domain.repository.DishRepository;
import com.restaurant.order_service.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;
}
