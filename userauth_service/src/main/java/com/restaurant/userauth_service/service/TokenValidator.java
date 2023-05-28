package com.restaurant.userauth_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.userauth_service.dto.jwt.JwtHeaderDto;
import com.restaurant.userauth_service.dto.jwt.JwtPayloadDto;
import org.apache.tomcat.util.codec.binary.Base64;

public class TokenValidator {

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
            hashed = JwtSignatureGenerator.getSignature(parts[0] + "." + parts[1], UserAuthConstants.SECRET);
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
}
