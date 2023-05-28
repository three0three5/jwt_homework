package com.restaurant.order_service.controller;

import com.restaurant.order_service.api.OrderApi;
import com.restaurant.order_service.dto.request.OrderInfoRequestDto;
import com.restaurant.order_service.dto.request.OrderRequestDto;
import com.restaurant.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class OrderController implements OrderApi {
    private final OrderService orderService;

    @Override
    public ResponseEntity<?> makeOrder(String token, OrderRequestDto orderRequestDto) {
        return null;
    }

    @Override
    public ResponseEntity<?> getOrderList(String token) {
        return null;
    }

    @Override
    public ResponseEntity<?> changeStatusById(String token, OrderInfoRequestDto requestDto) {
        return null;
    }

    @Override
    public ResponseEntity<?> getOrderInfo(String token, OrderInfoRequestDto requestDto) {
        return null;
    }
}
