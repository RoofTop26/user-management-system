package com.example.user_management_system.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = System.getenv("JWT_SECRET");

    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000;

    public static String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
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
    }
}