package com.restaurant.order_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.order_service.dto.jwt.JwtHeaderDto;
import com.restaurant.order_service.dto.jwt.JwtPayloadDto;
import com.restaurant.order_service.service.exception.InvalidOperationException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class TokenValidator {
    private static final String KEY = "gKYKxIeRfM8DUKpO2B4KbqLaflzGAhbfG/pK/ThoDbXi9BHrQpRTrUue2VaTS2OLVJQBhwIw30vMviloAh4Ebw==";


    private static final String ALGORITHM = "HmacSHA512";

    public static JwtPayloadDto getPayloadFromToken(String accessToken) throws InvalidOperationException {
        if (!TokenValidator.tokenIsValid(accessToken)) {
            throw new InvalidOperationException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        JwtPayloadDto payloadDto = TokenValidator.getPayload(accessToken);
        assert payloadDto != null;
        String payloadCheck = TokenValidator.checkPayload(payloadDto);
        if (!"ok".equals(payloadCheck)) {
            throw new InvalidOperationException(HttpStatus.UNAUTHORIZED, payloadCheck);
        }
        return payloadDto;
    }


    private static boolean tokenIsValid(String accessToken) {
        String[] parts = accessToken.split("\\.");
        if (parts.length != 3) {
            return false;
        }
        String header;
        String payload;
        String hashed;
        try {
            header = new String(Base64.decodeBase64(parts[0]));
            payload = new String(Base64.decodeBase64(parts[1]));
            hashed = getSignature(parts[0] + "." + parts[1]);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (!hashed.equals(parts[2])) {
            return false;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.readValue(header, JwtHeaderDto.class);
            objectMapper.readValue(payload, JwtPayloadDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static JwtPayloadDto getPayload(String accessToken) {
        String[] parts = accessToken.split("\\.");
        String payload = new String(Base64.decodeBase64(parts[1]));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(payload, JwtPayloadDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String checkPayload(JwtPayloadDto payloadDto) {
        Long notBefore = payloadDto.getNbf();
        Long expiredIn = payloadDto.getExp();
        Long current = System.currentTimeMillis();
        if (expiredIn < current) {
            return "token is expired";
        } else if (current < notBefore) {
            return "token creation fail";
        }
        return "ok";
    }
    private static String getSignature(String data) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(TokenValidator.KEY.getBytes(), ALGORITHM);
        Mac mac;
        try {
            mac = Mac.getInstance(ALGORITHM);
            mac.init(secretKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return java.util.Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes()));
    }
}
