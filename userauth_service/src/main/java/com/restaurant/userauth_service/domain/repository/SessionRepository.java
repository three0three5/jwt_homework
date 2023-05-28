package com.restaurant.userauth_service.domain.repository;

import com.restaurant.userauth_service.domain.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {

    boolean existsBySessionToken(String refreshToken);

    Session findBySessionToken(String refreshToken);

    boolean existsByUserId(Integer id);

    Session findByUserId(Integer id);
}
