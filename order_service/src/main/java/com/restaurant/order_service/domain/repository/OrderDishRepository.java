package com.restaurant.order_service.domain.repository;

import com.restaurant.order_service.domain.entity.OrderDish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDishRepository extends JpaRepository<OrderDish, Integer> {
    List<OrderDish> findAllByOrderId(Integer orderId);
}
