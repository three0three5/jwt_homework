package com.restaurant.userauth_service.api;

import com.restaurant.userauth_service.dto.request.UserIdRoleRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
public interface UserApi {
    @GetMapping("/get_info")
    ResponseEntity<?> checkAccess(@RequestHeader("Authorization") String accessToken);

    @GetMapping("/get_user_list")
    ResponseEntity<?> getUserList(@RequestHeader("Authorization") String accessToken);

    @PostMapping("/change_role")
    ResponseEntity<?> changeRoleById(@RequestHeader("Authorization") String accessToken,
                                     @RequestBody UserIdRoleRequestDto userIdRoleRequestDto);
}
