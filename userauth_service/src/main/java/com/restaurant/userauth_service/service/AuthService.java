package com.restaurant.userauth_service.service;

import com.restaurant.userauth_service.controller.exceptions.InvalidAuthOperationException;
import com.restaurant.userauth_service.domain.Role;
import com.restaurant.userauth_service.domain.entity.Session;
import com.restaurant.userauth_service.domain.entity.User;
import com.restaurant.userauth_service.domain.repository.SessionRepository;
import com.restaurant.userauth_service.domain.repository.UserRepository;
import com.restaurant.userauth_service.dto.request.LogInRequestDto;
import com.restaurant.userauth_service.dto.request.RegistrationRequestDto;
import com.restaurant.userauth_service.dto.response.JwtResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public JwtResponseDto getAccessToken(String refreshToken) throws InvalidAuthOperationException {
        if (!sessionRepository.existsBySessionToken(refreshToken)) {
            throw new InvalidAuthOperationException(HttpStatus.NOT_FOUND, UserAuthConstants.REFRESH_NOT_FOUND);
        }
        Session session = sessionRepository.findBySessionToken(refreshToken);
        if (session.getExpiresAt().before(Timestamp.valueOf(LocalDateTime.now()))) {
            sessionRepository.deleteById(session.getId());
            throw new InvalidAuthOperationException(HttpStatus.UNAUTHORIZED, UserAuthConstants.REFRESH_EXPIRED);
        }
        User user = session.getUser();
        return createNewToken(user);
    }

    public JwtResponseDto login(LogInRequestDto authRequest) throws InvalidAuthOperationException {
        boolean emailExists = userRepository.existsByEmail(authRequest.getEmail());
        if (!emailExists) {
            throw new InvalidAuthOperationException(HttpStatus.NOT_FOUND,
                    UserAuthConstants.EMAIL_NOT_FOUND);
        }
        User user = userRepository.findByEmail(authRequest.getEmail());
        String storedHashedPassword = user.getPasswordHash();
        if (!BCrypt.checkpw(authRequest.getPassword(), storedHashedPassword)) {
            throw new InvalidAuthOperationException(HttpStatus.CONFLICT,
                    UserAuthConstants.WRONG_PASSWORD);
        }
        // Получение токенов, сохранение сессии
        return createNewToken(user);
    }

    public String register(RegistrationRequestDto registrationRequestDto) throws InvalidAuthOperationException {
        // По умолчанию, после регистрации все пользователи имеют роль CUSTOMER
        // но пользователи с ролью MANAGER могут менять всем роли
        String userName = registrationRequestDto.getUserName();
        String email = registrationRequestDto.getEmail();
        checkUsernameAndEmail(userName, email);
        String passwordHash = BCrypt.hashpw(
                registrationRequestDto.getPassword(),
                BCrypt.gensalt()
        );
        userRepository.save(new User()
                .setRole(Role.CUSTOMER)
                .setEmail(email)
                .setUsername(userName)
                .setPasswordHash(passwordHash)
                .setCreatedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()))
        );
        return "Registration successful";
    }

    private void checkUsernameAndEmail(String userName, String email) throws InvalidAuthOperationException {
        if (!userName.matches("[a-zA-Z0-9\\s_]+")) {
            throw new InvalidAuthOperationException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    UserAuthConstants.USERNAME_IS_INVALID
            );
        }
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidAuthOperationException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    UserAuthConstants.EMAIL_IS_INVALID
            );
        }
        if (userRepository.existsByUsername(userName)) {
            throw new InvalidAuthOperationException(
                    HttpStatus.CONFLICT,
                    UserAuthConstants.USERNAME_IS_TAKEN
            );
        }
        if (userRepository.existsByEmail(email)) {
            throw new InvalidAuthOperationException(
                    HttpStatus.CONFLICT,
                    UserAuthConstants.EMAIL_IS_TAKEN
            );
        }
    }

    private JwtResponseDto createNewToken(User user) {
        String jsonEncoded = JwtSignatureGenerator.getJsonEncoded(user);
        String signature = JwtSignatureGenerator.getSignature(jsonEncoded, UserAuthConstants.SECRET);
        if ("".equals(signature)) {
            throw new RuntimeException("empty signature");
        }
        String jwtToken = jsonEncoded + "." + signature;
        String refresh = KeyGenerator.generateRefresh();
        // Обновление или сохранение сессии
        if (sessionRepository.existsByUserId(user.getId())) {
            Session session = sessionRepository.findByUserId(user.getId());
            session.setSessionToken(refresh)
                    .setExpiresAt(Timestamp.valueOf(LocalDateTime.now()
                            .plusMinutes(JwtSignatureGenerator.REFRESH_TOKEN_LIFETIME_MINUTES)));
            sessionRepository.save(session);
        } else {
            Session session = new Session()
                    .setSessionToken(refresh)
                    .setExpiresAt(Timestamp.valueOf(LocalDateTime.now()
                            .plusMinutes(JwtSignatureGenerator.REFRESH_TOKEN_LIFETIME_MINUTES)));
            user.setSession(session);
            session.setUser(user);
            userRepository.save(user);
        }
        return new JwtResponseDto()
                .setAccessToken(jwtToken)
                .setRefreshToken(refresh);
    }
}
