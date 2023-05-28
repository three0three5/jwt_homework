package com.restaurant.userauth_service.api;

import com.restaurant.userauth_service.dto.request.LogInRequestDto;
import com.restaurant.userauth_service.dto.request.RefreshJwtRequestDto;
import com.restaurant.userauth_service.dto.request.RegistrationRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth")
public interface AuthApi {
    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody RegistrationRequestDto registrationRequestDto);

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LogInRequestDto authRequest);

    @PostMapping("/token")
    ResponseEntity<?> getNewAccessAndRefreshToken(@RequestBody RefreshJwtRequestDto refreshRequest);
}
