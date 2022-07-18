package com.limited.sales.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@SpringBootTest
@TestPropertySource(value = "classpath:/application.properties")
class RedisServiceTest {

  @Autowired private RedisService redisService;

  @Test
  void getValues() {
    String values = redisService.getValue("ohjeung@naver.com");
    log.info(values);
  }
}
