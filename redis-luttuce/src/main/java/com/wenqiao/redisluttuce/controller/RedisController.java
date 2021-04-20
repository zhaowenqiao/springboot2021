package com.wenqiao.redisluttuce.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("redisTest")
    public String redisTest(){
        redisTemplate.opsForValue().set("key","hello");
        Object key = redisTemplate.opsForValue().get("key");
        log.info("value is:{}",key);
        return "Hello World!";
    }
}
