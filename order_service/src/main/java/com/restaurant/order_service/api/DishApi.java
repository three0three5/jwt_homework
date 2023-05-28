package com.restaurant.order_service.api;

import com.restaurant.order_service.dto.request.DishCreateRequestDto;
import com.restaurant.order_service.dto.request.DishDeleteRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/dish")
public interface DishApi {
    @GetMapping("/menu")
    ResponseEntity<?> getMenu(@RequestHeader("Authorization") String token);

    @PostMapping("/addOrChangeDish")
    ResponseEntity<?> addDish(@RequestHeader("Authorization") String token,
                              @RequestBody DishCreateRequestDto dishRequestDto);

    @PostMapping("/deleteDish")
    ResponseEntity<?> deleteDish(@RequestHeader("Authorization") String token,
                                 @RequestBody DishDeleteRequestDto requestDto);
}
