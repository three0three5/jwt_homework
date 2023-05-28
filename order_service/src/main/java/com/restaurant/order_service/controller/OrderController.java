package com.restaurant.order_service.controller;

import com.restaurant.order_service.api.OrderApi;
import com.restaurant.order_service.dto.request.OrderInfoRequestDto;
import com.restaurant.order_service.dto.request.OrderRequestDto;
import com.restaurant.order_service.service.OrderService;
import com.restaurant.order_service.service.exception.InvalidOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class OrderController implements OrderApi {
    private final OrderService orderService;

    @Override
    public ResponseEntity<?> makeOrder(String token, OrderRequestDto orderRequestDto) {
        try {
            return ResponseEntity.ok(orderService.makeOrder(token, orderRequestDto));
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getOrderList(String token) {
        try {
            return ResponseEntity.ok(orderService.getOrderList(token));
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> changeStatusById(String token, OrderInfoRequestDto requestDto) {
        try {
            return ResponseEntity.ok(orderService.changeOrderStatus(token, requestDto));
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getOrderInfo(String token, OrderInfoRequestDto requestDto) {
        try {
            return ResponseEntity.ok(orderService.getOrderInfo(token, requestDto));
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }
}
