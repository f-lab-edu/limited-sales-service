package com.limited.sales.redis;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class RedisServiceImpl implements RedisService {
  private final RedisTemplate<String, String> redisTemplate;

  public void setValue(String key, String data) {
    if (StringUtils.isBlank(key)) {
      throw new NullPointerException("키 값이 존재하지 않습니다.");
    }

    if (StringUtils.isBlank(data)) {
      throw new NullPointerException("데이터 값이 존재하지 않습니다.");
    }
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    valueOperations.set(key, data);
  }

  public String getValue(String key) {
    if (StringUtils.isBlank(key)) {
      throw new NullPointerException("키 값이 존재하지 않습니다.");
    }
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    return valueOperations.get(key);
  }

  public void deleteValue(String key) {
    if (StringUtils.isBlank(key)) {
      throw new NullPointerException("키 값이 존재하지 않습니다.");
    }
    redisTemplate.delete(key);
  }
}
