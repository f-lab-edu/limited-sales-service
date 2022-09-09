package com.limited.sales.redis;

public interface RedisService<V> {

  void deleteValue(String key);

  void setValue(String key, V data);

  V getValue(String key);
}
