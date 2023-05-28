package com.restaurant.userauth_service.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class RegistrationRequestDto {
    private String userName;
    private String email;
    private String password;
}
