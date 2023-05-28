package com.restaurant.userauth_service.service;

import com.restaurant.userauth_service.controller.exceptions.UserServiceException;
import com.restaurant.userauth_service.domain.Role;
import com.restaurant.userauth_service.domain.entity.User;
import com.restaurant.userauth_service.domain.repository.UserRepository;
import com.restaurant.userauth_service.dto.jwt.JwtPayloadDto;
import com.restaurant.userauth_service.dto.response.UserInfoResponseDto;
import com.restaurant.userauth_service.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfoResponseDto getUserInfo(String accessToken) throws UserServiceException {
        JwtPayloadDto payloadDto = getPayloadFromToken(accessToken);
        UserInfoResponseDto responseDto = new UserInfoResponseDto();
        responseDto.setUsername(payloadDto.getUsername())
                .setRole(payloadDto.getRole());
        switch (payloadDto.getRole()) {
            case MANAGER -> responseDto.setMessage(UserAuthConstants.MANAGER_MESSAGE);
            case CHEF -> responseDto.setMessage(UserAuthConstants.CHEF_MESSAGE);
            case CUSTOMER -> responseDto.setMessage(UserAuthConstants.CUSTOMER_MESSAGE);
        }
        return responseDto;
    }

    public List<UserResponseDto> getUserList(String accessToken) throws UserServiceException {
        JwtPayloadDto payloadDto = getPayloadFromToken(accessToken);
        if (!Objects.equals(Role.MANAGER, payloadDto.getRole())) {
            throw new UserServiceException(HttpStatus.FORBIDDEN, "You are not allowed to access this content");
        }
        List<User> users = userRepository.findAll();
        return users.stream().map(x -> new UserResponseDto()
                .setId(x.getId())
                .setRole(x.getRole())
                .setUsername(x.getUsername())
        ).collect(Collectors.toList());
    }

    public String changeUserRoleById(String accessToken, Integer userId, String roleStr) throws UserServiceException {
        JwtPayloadDto payloadDto = getPayloadFromToken(accessToken);
        if (!Objects.equals(Role.MANAGER, payloadDto.getRole())) {
            throw new UserServiceException(HttpStatus.FORBIDDEN, "You are not allowed to process this command");
        }
        Role role;
        try {
            role = Role.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            throw new UserServiceException(HttpStatus.BAD_REQUEST, "Invalid role");
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserServiceException(HttpStatus.NOT_FOUND, "User not found");
        }
        User user = optionalUser.get();
        user.setRole(role)
                .setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(user);
        return "Changed successfully";
    }

    private JwtPayloadDto getPayloadFromToken(String accessToken) throws UserServiceException {
        if (!TokenValidator.tokenIsValid(accessToken)) {
            throw new UserServiceException(HttpStatus.UNAUTHORIZED, UserAuthConstants.INVALID_ACCESS);
        }
        JwtPayloadDto payloadDto = TokenValidator.getPayload(accessToken);
        assert payloadDto != null;
        String payloadCheck = TokenValidator.checkPayload(payloadDto);
        if (!"ok".equals(payloadCheck)) {
            throw new UserServiceException(HttpStatus.UNAUTHORIZED, payloadCheck);
        }
        return payloadDto;
    }
}
