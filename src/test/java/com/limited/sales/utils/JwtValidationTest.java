package com.limited.sales.utils;

import com.auth0.jwt.algorithms.Algorithm;
import com.limited.sales.user.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.auth0.jwt.JWT.require;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:/application.properties")
@Transactional
@Slf4j
class JwtValidationTest {
  @Autowired private WebApplicationContext context;

  @Autowired MockMvc mockMvc;

  private String JwtAccessToken;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

    User user = User.builder().userEmail("test@test").userPassword("1234").build();

    JwtProvider utils = new JwtProvider();
    String accessTokenMethod = utils.createAccessTokenMethod(user);
    JwtAccessToken = JwtProperties.TOKEN_PREFIX + accessTokenMethod;
  }

  @Test
  void isValidationAccessTokenCheck() {
    JwtProvider jwtProvider = new JwtProvider();
    String token = jwtProvider.replaceTokenPrefix(JwtAccessToken);

    boolean result;
    try {
      require(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET)).build().verify(token);

      result = true;
    } catch (Exception e) {
      result = false;
    }

    Assertions.assertThat(result).isTrue();
  }

  @Test
  void isValidationRefreshTokenCheck() {}

  @Test
  void isValidationAuthorizationCheck() {}
}
