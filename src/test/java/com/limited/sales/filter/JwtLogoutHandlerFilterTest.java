package com.limited.sales.filter;

import com.google.gson.Gson;
import com.limited.sales.config.GsonSingleton;
import com.limited.sales.exception.sub.BadRequestException;
import com.limited.sales.exception.sub.TokenException;
import com.limited.sales.redis.RedisService;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProperties;
import com.limited.sales.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.properties")
@Slf4j
class JwtLogoutHandlerFilterTest {

  @Autowired MockMvc mockMvc;
  @Autowired RedisService redisService;
  Gson gson = GsonSingleton.getGson();
  String userAccessToken;

  @BeforeEach
  void setup() throws Exception {
    User user = User.builder().email("ohjeung@naver.com").password("1234").build();

    String token =
        mockMvc
            .perform(
                post("/login").content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andReturn()
            .getResponse()
            .getHeader(JwtProperties.HEADER_STRING);

    this.userAccessToken = token;
    assertThat(token).isNotNull().startsWith(JwtProperties.TOKEN_PREFIX);
  }

  @Test
  @DisplayName("JWT 로그아웃 - 정상적으로 로그아웃 확인, 로그아웃 후 재로그인 실패 확인")
  void logout() throws Exception {
    String token = JwtUtils.replaceTokenPrefix(this.userAccessToken);
    String email = JwtUtils.getClaim(token, JwtProperties.USER_EMAIL).asString();

    mockMvc
        .perform(post("/logout").header(JwtProperties.HEADER_STRING, this.userAccessToken))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());

    assertThat(redisService.getValue(email)).isNull();

    assertThatExceptionOfType(TokenException.class)
        .isThrownBy(
            () -> {
              mockMvc
                  .perform(
                      get("/test/user")
                          .header(JwtProperties.HEADER_STRING, this.userAccessToken)
                          .contentType(MediaType.APPLICATION_JSON))
                  .andExpect(status().is4xxClientError())
                  .andDo(print());
            })
        .withMessage("로그아웃 된 엑세스 토큰 입니다. 재로그인이 필요합니다.");
  }

  @Test
  @DisplayName("JWT 로그아웃 - 토큰 empty")
  void logoutTokenEmpty() throws Exception {
    assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(
            () -> {
              mockMvc
                  .perform(post("/logout").header(JwtProperties.HEADER_STRING, ""))
                  .andExpect(status().is4xxClientError())
                  .andDo(print());
            })
        .withMessage("토큰이 비었거나 존재하지 않습니다.");
  }


  @Test
  @DisplayName("JWT 로그아웃 - 토큰 NULL")
  void logoutTokenNull() throws Exception {
    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> {
              mockMvc
                  .perform(post("/logout").header(JwtProperties.HEADER_STRING, null))
                  .andExpect(status().is4xxClientError())
                  .andDo(print());
            });
  }

  @Test
  @DisplayName("JWT 로그아웃 - header none")
  void logoutHeaderNone() throws Exception {
    assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(
            () -> {
              mockMvc
                  .perform(post("/logout"))
                  .andExpect(status().is4xxClientError())
                  .andDo(print());
            })
        .withMessage("토큰이 비었거나 존재하지 않습니다.");
  }
}
