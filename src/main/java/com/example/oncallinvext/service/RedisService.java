package com.example.oncallinvext.service;


public interface RedisService {
    void enqueue(String queueName, String message);
    String dequeue(String queueName);
    String getLatestRecord(String queueName);
}
