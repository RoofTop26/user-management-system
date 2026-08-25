package com.example.user_management_system.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "week8-jwt-temporary-hardcoded-secret-key-day1-only-do-not-use-in-production";

    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public static String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .signWith(KEY)
                .compact();
    }

    public static String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public static boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String token = generateToken("admin1");
        System.out.println("Token: " + token);

        String username = extractUsername(token);
        System.out.println("Extracted username: " + username);
        System.out.println("Match admin1: " + "admin1".equals(username));

        System.out.println("isTokenValid(token): " + isTokenValid(token));
        System.out.println("isTokenValid(\"abc.def.ghi\"): " + isTokenValid("abc.def.ghi"));
    }
}