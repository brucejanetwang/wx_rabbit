package com.northtech.WeChat.test.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    @Autowired
    private StringRedisTemplate redisClient;

    @RequestMapping("/put")
    public  String redisput(){
        redisClient.opsForValue().set("key","value");
        return  "ok";
    }
    @RequestMapping("/get")
    public  String redisget(){
        return redisClient.opsForValue().get("key");
    }

}
