package com.restaurant.userauth_service.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Entity
@Table(name = "session")
@Data
@Accessors(chain = true)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "session_token")
    private String sessionToken;

    @Column(name = "expires_at")
    private Timestamp expiresAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
}
