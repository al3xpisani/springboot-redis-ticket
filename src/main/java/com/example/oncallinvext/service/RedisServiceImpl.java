package com.example.oncallinvext.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {
    private final StringRedisTemplate redisTemplate;
    public RedisServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void enqueue(String queueName, String message) {
        redisTemplate.opsForList().rightPush(queueName, message);
    }

    @Override
    public String dequeue(String queueName) {
        return redisTemplate.opsForList().leftPop(queueName);
    }

    @Override
    public String getLatestRecord(String queueName) {
        return redisTemplate.opsForList().index(queueName, -1);
    }
}
