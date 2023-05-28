package com.restaurant.userauth_service.controller;

import com.restaurant.userauth_service.api.AuthApi;
import com.restaurant.userauth_service.controller.exceptions.InvalidAuthOperationException;
import com.restaurant.userauth_service.dto.request.LogInRequestDto;
import com.restaurant.userauth_service.dto.request.RefreshJwtRequestDto;
import com.restaurant.userauth_service.dto.request.RegistrationRequestDto;
import com.restaurant.userauth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController implements AuthApi {

    public final AuthService authService;

    @Override
    public ResponseEntity<?> login(@RequestBody LogInRequestDto authRequest) {
        try {
            return ResponseEntity.ok(authService.login(authRequest));
        } catch (InvalidAuthOperationException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> register(@RequestBody RegistrationRequestDto registrationRequestDto) {
        try {
            return ResponseEntity.ok(authService.register(registrationRequestDto));
        } catch (InvalidAuthOperationException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getNewAccessAndRefreshToken(@RequestBody RefreshJwtRequestDto refreshRequest) {
        try {
            return ResponseEntity.ok(authService.getAccessToken(refreshRequest.getRefreshToken()));
        } catch (InvalidAuthOperationException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }
}
