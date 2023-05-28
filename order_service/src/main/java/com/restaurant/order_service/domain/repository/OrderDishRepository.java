package com.restaurant.order_service.domain.repository;

import com.restaurant.order_service.domain.entity.OrderDish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDishRepository extends JpaRepository<OrderDish, Integer> {
}
