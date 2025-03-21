package com.johnsoncskoo.shortify.service;

import org.springframework.http.HttpMethod;

public interface RateLimiterService {
    boolean tryConsume(String key, String path, String method);
    boolean tryConsume(String key, int limit, String method);
    long getRemainingLimit(String key, String path, String method);
}
