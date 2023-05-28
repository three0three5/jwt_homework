package com.restaurant.userauth_service.controller;

import com.restaurant.userauth_service.api.UserApi;
import com.restaurant.userauth_service.controller.exceptions.UserServiceException;
import com.restaurant.userauth_service.dto.request.UserIdRoleRequestDto;
import com.restaurant.userauth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    public final UserService userService;

    @Override
    public ResponseEntity<?> checkAccess(String accessToken) {
        try {
            return ResponseEntity.ok(userService.getUserInfo(accessToken));
        } catch (UserServiceException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getUserList(String accessToken) {
        try {
            return ResponseEntity.ok(userService.getUserList(accessToken));
        } catch (UserServiceException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> changeRoleById(String accessToken, UserIdRoleRequestDto userIdRoleRequestDto) {
        try {
            return ResponseEntity.ok(userService.changeUserRoleById(
                    accessToken,
                    userIdRoleRequestDto.getUserId(),
                    userIdRoleRequestDto.getRole()));
        } catch (UserServiceException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }
}
