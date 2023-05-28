package com.restaurant.userauth_service.domain.repository;

import com.restaurant.userauth_service.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

    boolean existsByUsername(String userName);
}
