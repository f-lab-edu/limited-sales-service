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
    if (StringUtils.isBlank(key) || StringUtils.isBlank(data)) {
      throw new IllegalArgumentException(String.format("키와 값이 존재하지 않습니다. - {key: %s, value: %s}", key, data));
    }

    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    valueOperations.set(key, data);
  }

  public String getValue(String key) {
    if (StringUtils.isBlank(key)) {
      throw new IllegalArgumentException(String.format("키가 존재하지 않습니다. - {key: %s}", key));
    }
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    return valueOperations.get(key);
  }

  public void deleteValue(String key) {
    if (StringUtils.isBlank(key)) {
      throw new IllegalArgumentException(String.format("키가 존재하지 않습니다. - {key: %s}", key));
    }
    redisTemplate.delete(key);
  }
}
