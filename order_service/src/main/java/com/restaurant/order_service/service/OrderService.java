package com.restaurant.order_service.service;

import com.restaurant.order_service.domain.repository.DishRepository;
import com.restaurant.order_service.domain.repository.OrderRepository;
import com.restaurant.order_service.dto.request.OrderInfoRequestDto;
import com.restaurant.order_service.dto.request.OrderRequestDto;
import com.restaurant.order_service.dto.response.OrderInfoResponseDto;
import com.restaurant.order_service.dto.response.OrderResponseDto;
import com.restaurant.order_service.service.exception.InvalidOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;

    public Object makeOrder(String token, OrderRequestDto orderRequestDto) throws InvalidOperationException {

    }


    public List<OrderResponseDto> getOrderList(String token) throws InvalidOperationException {

    }


    public String changeOrderStatus(String token,
                                    OrderInfoRequestDto requestDto) throws InvalidOperationException {

    }

    public OrderInfoResponseDto getOrderInfo(String token,
                                             OrderInfoRequestDto requestDto) throws InvalidOperationException {

    }
}
