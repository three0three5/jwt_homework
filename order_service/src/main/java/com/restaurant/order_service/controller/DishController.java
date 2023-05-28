package com.restaurant.order_service.controller;

import com.restaurant.order_service.api.DishApi;
import com.restaurant.order_service.dto.request.DishCreateRequestDto;
import com.restaurant.order_service.dto.request.DishDeleteRequestDto;
import com.restaurant.order_service.service.DishService;
import com.restaurant.order_service.service.exception.InvalidOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DishController implements DishApi {

    private final DishService dishService;

    @Override
    public ResponseEntity<?> getMenu(String token) {
        try {
            return ResponseEntity.ok(dishService.getMenu(token));
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> addDish(String token, DishCreateRequestDto dishRequestDto) {
        try {
            return ResponseEntity.ok(dishService.addDish(token, dishRequestDto));
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteDish(String token, DishDeleteRequestDto requestDto) {
        try {
            return ResponseEntity.ok(dishService.deleteDish(token, requestDto));
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }
}
