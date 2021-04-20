package com.wenqiao.redisjedis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.redis.jedis.pool")
public class RedisPoolData {
    private Integer maxIdle;
    private Integer minIdle;
    private Integer maxTotal;
    private Long maxWait;
}
