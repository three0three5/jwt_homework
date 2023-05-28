package com.restaurant.order_service.service;

import com.restaurant.order_service.domain.Role;
import com.restaurant.order_service.domain.entity.Dish;
import com.restaurant.order_service.domain.repository.DishRepository;
import com.restaurant.order_service.dto.jwt.JwtPayloadDto;
import com.restaurant.order_service.dto.request.DishCreateRequestDto;
import com.restaurant.order_service.dto.request.DishDeleteRequestDto;
import com.restaurant.order_service.dto.response.DishClientResponseDto;
import com.restaurant.order_service.dto.response.DishManagerResponseDto;
import com.restaurant.order_service.dto.response.DishResponseDto;
import com.restaurant.order_service.service.exception.InvalidOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;

    public List<? extends DishResponseDto> getMenu(String token) throws InvalidOperationException {
        JwtPayloadDto payloadDto = TokenValidator.getPayloadFromToken(token);
        List<Dish> dishes = dishRepository.findAll();
        if (Objects.equals(payloadDto.getRole(), Role.MANAGER)) {
            return dishes.stream().map(x -> new DishManagerResponseDto()
                    .setId(x.getId())
                    .setQuantity(x.getQuantity())
                    .setName(x.getName())
                    .setDescription(x.getDescription())
                    .setPrice(x.getPrice())
            ).collect(Collectors.toList());
        }
        return dishes.stream().map(x -> new DishClientResponseDto()
                .setAvailable(x.getQuantity() > 0)
                .setPrice(x.getPrice())
                .setName(x.getName())
                .setDescription(x.getDescription())
        ).collect(Collectors.toList());
    }

    public String addDish(String token, DishCreateRequestDto dishRequestDto) throws InvalidOperationException {
        JwtPayloadDto payloadDto = TokenValidator.getPayloadFromToken(token);
        if (!Objects.equals(Role.MANAGER, payloadDto.getRole())) {
            throw new InvalidOperationException(HttpStatus.FORBIDDEN, "You are not allowed to do this");
        }
        if (dishRepository.existsByName(dishRequestDto.getName())) {
            Dish dish = dishRepository.findByName(dishRequestDto.getName());
            dish.setDescription(dishRequestDto.getDescription())
                    .setPrice(dishRequestDto.getPrice())
                    .setQuantity(dishRequestDto.getQuantity());
            dishRepository.save(dish);
            return "updated";
        }
        dishRepository.save(new Dish()
                    .setDescription(dishRequestDto.getDescription())
                    .setPrice(dishRequestDto.getPrice())
                    .setQuantity(dishRequestDto.getQuantity())
                    .setName(dishRequestDto.getName())
        );
        return "saved";
    }

    public String deleteDish(String token, DishDeleteRequestDto requestDto) throws InvalidOperationException {
        JwtPayloadDto payloadDto = TokenValidator.getPayloadFromToken(token);
        if (!Objects.equals(Role.MANAGER, payloadDto.getRole())) {
            throw new InvalidOperationException(HttpStatus.FORBIDDEN, "You are not allowed to this");
        }
        if (!dishRepository.existsByName(requestDto.getDishName())) {
            throw new InvalidOperationException(HttpStatus.NOT_FOUND, "Nothing to delete");
        }
        dishRepository.deleteByName(requestDto.getDishName());
        return "Success";
    }
}
