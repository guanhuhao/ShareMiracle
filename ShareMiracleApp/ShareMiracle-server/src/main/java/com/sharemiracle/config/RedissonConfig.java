package com.sharemiracle.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Value("${miracle.redis.host}")
    private String redisHost;

    @Value("${miracle.redis.port}")
    private int redisPort;

    @Value("${miracle.redis.password}")
    private String redisPassword;
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisHost + ":" + redisPort)
                .setPassword(redisPassword);

        return Redisson.create(config);
    }
}
