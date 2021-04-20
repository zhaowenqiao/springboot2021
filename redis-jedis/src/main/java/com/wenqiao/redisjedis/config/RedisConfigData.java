package com.wenqiao.redisjedis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfigData {
    private String host;
    private Integer port;
    private String password;
    private Integer timeout;
    private Integer dataBase;
}
