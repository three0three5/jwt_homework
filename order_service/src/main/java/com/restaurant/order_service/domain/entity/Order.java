package com.restaurant.order_service.domain.entity;

import com.restaurant.order_service.domain.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "order")
@Data
@Accessors(chain = true)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "nickname")
    private String nickname; // Я считаю, что id из бд другого микросервиса этому не нужен,
                             // потому что он не обязан что-то знать о бд к которой он не относится.
                             // Поэтому хранится уникальный никнейм
    @Column(name = "special_requests")
    private String specialRequests;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderDish> orderDishes;
}
