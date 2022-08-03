package com.limited.sales.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public final class RedisServiceImpl implements RedisService {
  private final RedisTemplate<String, String> redisTemplate;

  public void setValue(String key, String data) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    valueOperations.set(key, data);
  }


  public void setValues(String key, String data, Duration duration) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    valueOperations.set(key, data, duration);
  }

  public String getValue(String key) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    return valueOperations.get(key);
  }

  public void deleteValue(String key) {
    redisTemplate.delete(key);
  }
}
