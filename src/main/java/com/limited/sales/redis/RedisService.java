package com.limited.sales.redis;

public interface RedisService {

  void setValue(String key, String data);

  String getValue(String key);

  void deleteValue(String key);
}
