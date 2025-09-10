package com.arka.msauth.auth.util;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {

    private static final String SECRET_KEY = "secretKey123";
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    @SuppressWarnings("deprecation")
    public static String generateToken(String username, Long clientId){
        return Jwts.builder()
                .setSubject(username)
                .claim("clientId", clientId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }


}
