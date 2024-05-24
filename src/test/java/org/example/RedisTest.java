package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest//如果测试类上有这个注解，那么单元测试之前，会初始化pring
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Test
    public void testSet(){
        //往redis中存储一个键值对
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set("username","zhangsan");

        //15秒过期
        operations.set("id","1",15, TimeUnit.SECONDS);
    }


    @Test
    public void testGet(){
        //往redis中存储一个键值对
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();

        //获取redis中key为id的值
        System.out.println(operations.get("id"));

    }
}
