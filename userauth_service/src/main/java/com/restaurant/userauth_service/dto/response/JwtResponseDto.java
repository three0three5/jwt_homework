package com.restaurant.userauth_service.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class JwtResponseDto {
    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;
}
