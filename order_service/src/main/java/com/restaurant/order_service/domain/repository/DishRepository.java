package com.restaurant.order_service.domain.repository;

import com.restaurant.order_service.domain.entity.Dish;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    boolean existsByName(String name);

    Dish findByName(String name);

    @Transactional
    void deleteByName(String dishName);
}
