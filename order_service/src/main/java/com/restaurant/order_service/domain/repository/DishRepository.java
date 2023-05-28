package com.restaurant.order_service.domain.repository;

import com.restaurant.order_service.domain.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Integer> {
}
