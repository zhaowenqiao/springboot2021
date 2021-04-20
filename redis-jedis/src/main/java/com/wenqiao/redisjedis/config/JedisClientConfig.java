package com.wenqiao.redisjedis.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Slf4j
@Configuration
public class JedisClientConfig {
    @Autowired
    private RedisConfigData redisConfigData;

    @Autowired
    private RedisPoolData redisPoolData;

    /**
     * 单节点
     *
     * @return
     */
    @Bean
    public RedisConnectionFactory defaultJedisConnectionFactory() {

        log.info("defaultJedisConnectionFactory redisConfigData is:{}", redisConfigData);
        log.info("defaultJedisConnectionFactory redisPoolData is:{}", redisPoolData);
        if(redisConfigData == null || redisPoolData == null){
            throw new IllegalAccessError("redis config data is empty,please check redisConfig.properties");
        }
        //如果什么参数都不设置，默认连接本地6379端口
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
        standaloneConfiguration.setHostName(redisConfigData.getHost());
        standaloneConfiguration.setPort(Integer.valueOf(redisConfigData.getPort()));
        standaloneConfiguration.setPassword(redisConfigData.getPassword());
        standaloneConfiguration.setDatabase(redisConfigData.getDataBase());
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();

        //最大空闲
        genericObjectPoolConfig.setMaxIdle(redisPoolData.getMaxIdle());
        //链接池中最大连接数
        genericObjectPoolConfig.setMaxTotal(redisPoolData.getMaxTotal());
        //最小空闲
        genericObjectPoolConfig.setMinIdle(redisPoolData.getMinIdle());
        //连接池资源耗尽后  最大等待时间
        genericObjectPoolConfig.setMaxWaitMillis(redisPoolData.getMaxWait());

        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder()
                .connectTimeout(Duration.ofMillis(redisConfigData.getTimeout()))
                .readTimeout(Duration.ofMillis(5000))
                .usePooling().poolConfig(genericObjectPoolConfig)
                .build();

        return new JedisConnectionFactory(standaloneConfiguration, jedisClientConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> defaultRedisTemplate(RedisConnectionFactory defaultJedisConnectionFactory) {
        //创建一个模板类
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        //将刚才的redis连接工厂设置到模板类中
        template.setConnectionFactory(defaultJedisConnectionFactory);
        StringRedisSerializer hashKeySerializer = new StringRedisSerializer();
        template.setKeySerializer(hashKeySerializer);
        template.setHashKeySerializer(hashKeySerializer);
        return template;
    }

    @Bean
    public StringRedisTemplate defaultStringRedisTemplate(RedisConnectionFactory defaultJedisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(defaultJedisConnectionFactory);
        return stringRedisTemplate;
    }
}
