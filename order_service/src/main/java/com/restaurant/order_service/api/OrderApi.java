package com.restaurant.order_service.api;

import com.restaurant.order_service.dto.request.OrderInfoRequestDto;
import com.restaurant.order_service.dto.request.OrderRequestDto;
import com.restaurant.order_service.dto.request.OrderStatusRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/order")
public interface OrderApi {
    @PostMapping("/makeOrder")
    ResponseEntity<?> makeOrder(@RequestHeader("Authorization") String token,
                                @RequestBody OrderRequestDto orderRequestDto);

    @PostMapping("/getOrders")
    ResponseEntity<?> getOrderList(@RequestHeader("Authorization") String token);

    @PostMapping("/changeStatus")
    ResponseEntity<?> changeStatusById(@RequestHeader("Authorization") String token,
                                       @RequestBody OrderStatusRequestDto requestDto);

    @PostMapping("/getOrderInfo")
    ResponseEntity<?> getOrderInfo(@RequestHeader("Authorization") String token,
                                   @RequestBody OrderInfoRequestDto requestDto);
}
