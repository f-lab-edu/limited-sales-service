package com.limited.sales;

import com.limited.sales.config.RedisConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(value = "classpath:/application.properties")
public class BeanContextView {
  //    @Autowired
  //    DefaultListableBeanFactory context;
  //    @Autowired
  //    RedisTemplate<String, String> redisTemplate;

  @Test
  void view() {
    ApplicationContext context = new AnnotationConfigApplicationContext(RedisConfig.class);
    RedisTemplate<String, String> template = context.getBean(RedisTemplate.class);
    // RedisTemplate<String, String> redisTemplate = (RedisTemplate<String, String>)
    // context.getBean("redisTemplate");
    System.out.println(template.opsForValue().get("ohjeung@naver.com"));
  }
}
