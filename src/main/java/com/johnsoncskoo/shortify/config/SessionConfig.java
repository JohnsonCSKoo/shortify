package com.johnsoncskoo.shortify.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@RequiredArgsConstructor
public class SessionConfig {

    @Value("${spring.data.redis.host}")
    private String REDIS_HOST;

    @Value("${spring.data.redis.port}")
    private String REDIS_PORT = String.valueOf(6379);

    @Value("${spring.data.redis.password}")
    private String REDIS_PASSWORD;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration(
                        REDIS_HOST,
                        Integer.parseInt(REDIS_PORT)
                );
        redisStandaloneConfiguration.setDatabase(0);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(REDIS_PASSWORD)); // connection timeout

        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
}
