package com.limited.sales.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class TokenRedisServiceImpl implements RedisService{
    private final RedisTemplate<String, String> redisTemplate;

    public void setValues(String key, String data){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, data);
    }

    public String getValues(String key){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void deleteValues(String key){
        redisTemplate.delete(key);
    }
}
