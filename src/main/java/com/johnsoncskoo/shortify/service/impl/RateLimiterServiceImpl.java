package com.johnsoncskoo.shortify.service.impl;

import com.johnsoncskoo.shortify.config.RateLimitConfig;
import com.johnsoncskoo.shortify.service.RateLimiterService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.distributed.serialization.Mapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RateLimiterServiceImpl implements RateLimiterService {

    private final ProxyManager<byte[]> byteProxyManager;
    private ProxyManager<String> proxyManager;
    private final RateLimitConfig config;

    @PostConstruct
    public void init() {
        proxyManager = byteProxyManager.withMapper(Mapper.STRING::toBytes);
    }

    @Override
    public boolean tryConsume(String key, String path, String method) {
        int limit = config.getLimitForEndpoint(path, method);
        return proxyManager.builder()
                .build(key, () -> getBucketConfiguration(limit))
                .tryConsume(1);
    }

    @Override
    public boolean tryConsume(String key, int limit, String method) {
        return proxyManager.builder()
                .build(key, () -> getBucketConfiguration(limit))
                .tryConsume(1);
    }

    @Override
    public long getRemainingLimit(String key, String path, String method) {
        int limit = config.getLimitForEndpoint(path, method);
        return proxyManager.builder()
                .build(key, () -> getBucketConfiguration(limit))
                .getAvailableTokens();
    }

    private BucketConfiguration getBucketConfiguration(int limit) {
        return BucketConfiguration.builder()
                .addLimit(Bandwidth.simple(limit, Duration.ofMinutes(1)))
                .build();
    }
}
