package com.restaurant.userauth_service.dto.jwt;

import com.restaurant.userauth_service.domain.entity.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class JwtPayloadDto {
    private String iss;
    private Long exp;
    private Long nbf;
    private Role role;
    private String username;
}
