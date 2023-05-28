package com.restaurant.order_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.order_service.dto.jwt.JwtHeaderDto;
import com.restaurant.order_service.dto.jwt.JwtPayloadDto;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Setter
public class TokenValidator {
    @Value("${secret.key}")
    private static String KEY;

    private static final String ALGORITHM = "HmacSHA512";


    public static boolean tokenIsValid(String accessToken) {
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
            hashed = getSignature(parts[0] + "." + parts[1], KEY);
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

    public static JwtPayloadDto getPayload(String accessToken) {
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

    public static String checkPayload(JwtPayloadDto payloadDto) {
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
    private static String getSignature(String data, String key) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
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
