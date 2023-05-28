package com.restaurant.userauth_service.dto.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class JwtHeaderDto {
    private String typ;
    private String alg;
}
