package com.restaurant.order_service.service;

import com.restaurant.order_service.domain.OrderStatus;
import com.restaurant.order_service.domain.Role;
import com.restaurant.order_service.domain.entity.Dish;
import com.restaurant.order_service.domain.entity.Order;
import com.restaurant.order_service.domain.entity.OrderDish;
import com.restaurant.order_service.domain.repository.DishRepository;
import com.restaurant.order_service.domain.repository.OrderDishRepository;
import com.restaurant.order_service.domain.repository.OrderRepository;
import com.restaurant.order_service.dto.jwt.JwtPayloadDto;
import com.restaurant.order_service.dto.request.DishRequestDto;
import com.restaurant.order_service.dto.request.OrderInfoRequestDto;
import com.restaurant.order_service.dto.request.OrderRequestDto;
import com.restaurant.order_service.dto.response.DishManagerResponseDto;
import com.restaurant.order_service.dto.response.OrderInfoResponseDto;
import com.restaurant.order_service.dto.response.OrderResponseDto;
import com.restaurant.order_service.service.exception.InvalidOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;
    private final OrderDishRepository orderDishRepository;

    public synchronized OrderInfoResponseDto makeOrder(String token, OrderRequestDto orderRequestDto)
            throws InvalidOperationException {
        JwtPayloadDto payloadDto = TokenValidator.getPayloadFromToken(token);
        if (!Objects.equals(payloadDto.getRole(), Role.CUSTOMER)) {
            throw new InvalidOperationException(HttpStatus.FORBIDDEN, "Not a customer");
        }
        List<DishRequestDto> dishRequestDtoList = orderRequestDto.getDishRequestDtoList()
                .stream()
                .collect(Collectors.toMap(
                        DishRequestDto::getName,
                        Function.identity(),
                        (d1, d2) -> new DishRequestDto().setName(d1.getName())
                                .setQuantity(d1.getQuantity() + d2.getQuantity())
                ))
                .values()
                .stream()
                .toList();
        for (var dishRequestDto : dishRequestDtoList) {
            if (!dishRepository.existsByName(dishRequestDto.getName())) {
                throw new InvalidOperationException(HttpStatus.BAD_REQUEST,
                        "No such dish: " + dishRequestDto.getName());
            }
            Dish dish = dishRepository.findByName(dishRequestDto.getName());
            if (dish.getQuantity() < dishRequestDto.getQuantity()) {
                throw new InvalidOperationException(HttpStatus.BAD_REQUEST,
                        "Sorry, we cant provide more than " + dish.getQuantity() + " of these: "
                                + dish.getName());
            }
        }
        Order order = new Order().setStatus(OrderStatus.IN_PROGRESS)
                .setNickname(payloadDto.getUsername())
                .setCreatedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setSpecialRequests(orderRequestDto.getSpecialRequests())
                .setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        order = orderRepository.save(order);
        List<DishManagerResponseDto> resultList = new ArrayList<>();
        for (var dishRequestDto : dishRequestDtoList) {
            Dish dish = dishRepository.findByName(dishRequestDto.getName());
            dish.setQuantity(dish.getQuantity() - dishRequestDto.getQuantity());
            dishRepository.save(dish);
            orderDishRepository.save(new OrderDish()
                    .setOrder(order)
                    .setDish(dish)
                    .setPrice(dish.getPrice())
                    .setQuantity(dishRequestDto.getQuantity())
            );
            DishManagerResponseDto responseDto = new DishManagerResponseDto();
            responseDto.setDescription(dish.getDescription());
            responseDto.setName(dish.getName());
            responseDto.setPrice(dish.getPrice());
            resultList.add(responseDto
                    .setId(dish.getId())
                    .setQuantity(dishRequestDto.getQuantity())
            );
        }
        return new OrderInfoResponseDto()
                .setOrderId(order.getId())
                .setStatus(order.getStatus())
                .setUsername(payloadDto.getUsername())
                .setDishList(resultList)
                .setSpecialRequests(order.getSpecialRequests());
    }


    public List<OrderResponseDto> getOrderList(String token) throws InvalidOperationException {
        return null;
    }


    public String changeOrderStatus(String token,
                                    OrderInfoRequestDto requestDto) throws InvalidOperationException {
        return null;
    }

    public OrderInfoResponseDto getOrderInfo(String token,
                                             OrderInfoRequestDto requestDto) throws InvalidOperationException {
        return null;
    }
}
