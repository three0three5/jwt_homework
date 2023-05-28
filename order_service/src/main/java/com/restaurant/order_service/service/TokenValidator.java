package com.restaurant.order_service.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Setter
public class TokenValidator {
    @Value("${secret.key}")
    private static String KEY;


}
