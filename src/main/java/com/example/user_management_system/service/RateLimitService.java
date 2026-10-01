package com.example.user_management_system.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {

    private final StringRedisTemplate redisTemplate;

    public RateLimitService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String key, long cooldownSeconds) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.ofSeconds(cooldownSeconds));
        return Boolean.TRUE.equals(success);
    }

    public boolean isAllowed(String key, int maxAttempts, long windowSeconds) {
        long currentWindow = System.currentTimeMillis() / 1000 / windowSeconds;
        String windowKey = key + ":" + currentWindow;

        Long count = redisTemplate.opsForValue().increment(windowKey);
        if (count != null && count == 1) {
            redisTemplate.expire(windowKey, Duration.ofSeconds(windowSeconds));
        }

        return count != null && count <= maxAttempts;
    }
}
