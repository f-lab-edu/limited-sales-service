package com.limited.sales.redis;

public interface RedisService {

    void setValues(String key, String data);

    String getValues(String key);

    void deleteValues(String key);

}
