package com.restaurant.userauth_service.controller.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
public class UserServiceException extends Exception {
    private HttpStatus httpStatus;
    private String message;
}
