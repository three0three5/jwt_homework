package com.restaurant.userauth_service.app;

import com.restaurant.userauth_service.service.UserAuthConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.restaurant.userauth_service.domain.repository")
@EntityScan("com.restaurant.userauth_service.*")
@ComponentScan(basePackages = "com.restaurant.userauth_service.*")
public class UserauthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserauthServiceApplication.class, args);
	}

}
