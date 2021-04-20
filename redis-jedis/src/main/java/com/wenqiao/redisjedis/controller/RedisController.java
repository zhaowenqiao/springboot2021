package com.wenqiao.redisjedis.controller;

import com.wenqiao.redisjedis.config.StringRedisTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private StringRedisTemplateService stringRedisTemplateService;

    @RequestMapping("/redisTest")
    public String redisTest(){
        stringRedisTemplateService.set("key","hello");
        String value = stringRedisTemplateService.get("key");
        log.info("value is:{}", value);
        return "Hello World!";
    }

}
