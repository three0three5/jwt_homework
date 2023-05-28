package com.restaurant.userauth_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.userauth_service.domain.entity.User;
import com.restaurant.userauth_service.dto.jwt.JwtHeaderDto;
import com.restaurant.userauth_service.dto.jwt.JwtPayloadDto;
import io.jsonwebtoken.io.Encoders;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class JwtSignatureGenerator {

    public static final int ACCESS_TOKEN_LIFETIME_MINUTES = 2;
    public static final int REFRESH_TOKEN_LIFETIME_MINUTES = 60;
    private static final String ALGORITHM = "HmacSHA512";

    public static String getSignature(String data, String key) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Mac mac;
        try {
            mac = Mac.getInstance(ALGORITHM);
            mac.init(secretKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes()));
    }

    public static Long getExpiredIn(int mins) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, mins);
        return calendar.getTimeInMillis();
    }

    public static String getJsonEncoded(User user) {
        JwtHeaderDto headerDto = new JwtHeaderDto()
                .setAlg("HS512")
                .setTyp("JWT");

        Long expireIn = getExpiredIn(ACCESS_TOKEN_LIFETIME_MINUTES);

        JwtPayloadDto payloadDto = new JwtPayloadDto()
                .setRole(user.getRole())
                .setUsername(user.getUsername())
                .setNbf(System.currentTimeMillis())
                .setIss("Auth Server")
                .setExp(expireIn);

        return getJsonEncoded(headerDto, payloadDto);
    }

    private static String getJsonEncoded(JwtHeaderDto headerDto, JwtPayloadDto payloadDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonEncoded;
        try {
            jsonEncoded = Encoders.BASE64.encode(objectMapper.writeValueAsString(headerDto).getBytes()) +
                    "." +
                    Encoders.BASE64.encode(objectMapper.writeValueAsString(payloadDto).getBytes());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonEncoded;
    }
}
