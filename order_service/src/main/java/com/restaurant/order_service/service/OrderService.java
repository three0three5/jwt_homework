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
import com.restaurant.order_service.dto.request.OrderStatusRequestDto;
import com.restaurant.order_service.dto.response.DishIdDto;
import com.restaurant.order_service.dto.response.OrderInfoResponseDto;
import com.restaurant.order_service.dto.response.OrderResponseDto;
import com.restaurant.order_service.service.exception.InvalidOperationException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;
    private final OrderDishRepository orderDishRepository;

    @PostConstruct
    public void init() {
        Thread thread = new Thread(new OrdersExecutor(orderRepository));
        thread.start();
    }

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
        List<DishIdDto> resultList = new ArrayList<>();
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
            resultList.add(new DishIdDto()
                    .setPrice(dish.getPrice())
                    .setId(dish.getId())
                    .setQuantity(dish.getQuantity())
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
        JwtPayloadDto payloadDto = TokenValidator.getPayloadFromToken(token);
        List<Order> orders = orderRepository.findAll();
        List<OrderResponseDto> result = orders.stream()
                .map(x -> new OrderResponseDto()
                        .setId(x.getId())
                        .setUsername(x.getNickname())
                        .setStatus(x.getStatus())
                        .setUpdatedAt(x.getUpdatedAt())
                        .setSpecialRequests(x.getSpecialRequests())
                )
                .toList();
        if (Objects.equals(payloadDto.getRole(), Role.CUSTOMER)) {
            result = result.stream()
                    .filter(x -> x.getUsername().equals(payloadDto.getUsername()))
                    .toList();
        }
        return result;
    }


    public String changeOrderStatus(String token,
                                    OrderStatusRequestDto requestDto) throws InvalidOperationException {
        JwtPayloadDto payloadDto = TokenValidator.getPayloadFromToken(token);
        if (!Objects.equals(payloadDto.getRole(), Role.CHEF)) {
            throw new InvalidOperationException(HttpStatus.FORBIDDEN, "You are not allowed to it");
        }
        Optional<Order> orderOptional = orderRepository.findById(requestDto.getOrderId());
        if (orderOptional.isEmpty()) {
            throw new InvalidOperationException(HttpStatus.NOT_FOUND, "Incorrect Order Id");
        }
        Order order = orderOptional.get();
        try {
            order.setStatus(OrderStatus.valueOf(requestDto.getStatus()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidOperationException(HttpStatus.BAD_REQUEST, "Wrong role");
        }
        orderRepository.save(order);
        return "success";
    }

    public OrderInfoResponseDto getOrderInfo(String token,
                                             OrderInfoRequestDto requestDto) throws InvalidOperationException {
        JwtPayloadDto payloadDto = TokenValidator.getPayloadFromToken(token);
        List<OrderDish> dishes = orderDishRepository.findAllByOrderId(requestDto.getOrderId());
        if (dishes.isEmpty()) {
            throw new InvalidOperationException(HttpStatus.NOT_FOUND, "No such order");
        }
        Optional<Order> orderOptional = orderRepository.findById(requestDto.getOrderId());
        if (orderOptional.isEmpty()) {
            throw new InvalidOperationException(HttpStatus.NOT_FOUND, "No such order");
        }
        Order order = orderOptional.get();
        if (Objects.equals(payloadDto.getRole(), Role.CUSTOMER) &&
                !order.getNickname().equals(payloadDto.getUsername())) {
            throw new InvalidOperationException(HttpStatus.FORBIDDEN, "Not allowed");
        }
        List<DishIdDto> resultList = dishes.stream()
                .map(x -> new DishIdDto()
                        .setId(x.getId())
                        .setPrice(x.getPrice())
                        .setQuantity(x.getQuantity())
                )
                .toList();
        return new OrderInfoResponseDto()
                .setDishList(resultList)
                .setOrderId(requestDto.getOrderId())
                .setStatus(order.getStatus())
                .setUsername(order.getNickname())
                .setSpecialRequests(order.getSpecialRequests());
    }
}
