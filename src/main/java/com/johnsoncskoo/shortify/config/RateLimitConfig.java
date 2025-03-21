package com.johnsoncskoo.shortify.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitConfig {
    private Map<String, EndpointLimit> endpoints = new HashMap<>();
    private int defaultLimit = 20;

    public int getLimitForEndpoint(String path, String method) {
        // check for exact match
        if (endpoints.containsKey(path)) {
            EndpointLimit item = endpoints.get(path);
            return item.limit;
        }

        // check for pattern matches
        for (Map.Entry<String, EndpointLimit> entry : endpoints.entrySet()) {
            String pattern = entry.getKey();
            if (pattern.endsWith("/**") && path.startsWith(pattern.substring(0, pattern.length() - 3))) {
                EndpointLimit item = entry.getValue();
            }
        }

        return defaultLimit;
    }

    static class EndpointLimit {
        private String method;
        private int limit;
    }
}
