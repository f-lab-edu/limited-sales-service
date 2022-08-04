package com.limited.sales.token;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestPropertySource(value = "classpath:/application.properties")
@Transactional
@Slf4j
class TokenServiceImplTest {

  @Autowired TokenService tokenService;

  /**
   *
   *
   * <h1>리프레쉬 토큰 삭제 비즈니스 로직</h1>
   */
  @Test
  @DisplayName("토큰 비즈니스 - 리프레쉬 토큰 삭제")
  void deleteRefreshToken() {}

  @Test
  void blacklistAccessToken() {}
}
