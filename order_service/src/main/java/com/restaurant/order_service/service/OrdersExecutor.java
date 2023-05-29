package com.restaurant.order_service.service;


import com.restaurant.order_service.domain.OrderStatus;
import com.restaurant.order_service.domain.entity.Order;
import com.restaurant.order_service.domain.repository.OrderRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class OrdersExecutor implements Runnable {
    private final OrderRepository orderRepository;

    @Override
    public void run() {
        while (true) {
            System.out.println("Processing next order\n");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Order order = orderRepository.findFirstByStatus(OrderStatus.PENDING);
            if (order == null) {
                continue;
            }
            order.setStatus(OrderStatus.COMPLETED);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            orderRepository.save(order);
        }
    }
}
