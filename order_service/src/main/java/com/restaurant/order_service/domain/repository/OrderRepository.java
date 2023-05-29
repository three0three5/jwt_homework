package com.restaurant.order_service.domain.repository;

import com.restaurant.order_service.domain.OrderStatus;
import com.restaurant.order_service.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findFirstByStatus(OrderStatus orderStatus);
}
