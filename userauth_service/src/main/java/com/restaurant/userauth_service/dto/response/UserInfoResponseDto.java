package com.restaurant.userauth_service.dto.response;

import com.restaurant.userauth_service.domain.entity.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class UserInfoResponseDto {
    private String username;
    private Role role;
    private String message;
}
