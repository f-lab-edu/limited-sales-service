package com.limited.sales.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.limited.sales.config.GsonSingleton;
import com.limited.sales.exception.sub.TokenException;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProperties;
import com.limited.sales.utils.JwtUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.properties")
class JwtAuthorizationFilterTest {

  private final Gson gson = GsonSingleton.getGson();
  @Autowired private MockMvc mockMvc;

  /**
   *
   *
   * <h1>테스트용 토큰 생성</h1>
   *
   * @param email 사용자 이메일
   * @param password 비밀번호
   * @return 토큰값
   */
  private String createTestToken(String email, String password) {
    User user = User.builder().email(email).email(password).build();
    return JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(user);
  }

  /**
   *
   *
   * <h1>인가 필터 토큰 검증</h1>
   */
  @Test
  @DisplayName("JWT인가필터 - JWT 토큰 검증 확인 성공")
  void doFilterInternal() throws Exception {
    String prefixToken = JwtProperties.TOKEN_PREFIX + createTestToken("test@test", "1234");

    mockMvc
        .perform(
            get("/test/none")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());
  }

  @Test
  @DisplayName("JWT인가필터 - JWT 토큰 권한 없음 확인")
  void doFilterInternalNoneAuth() throws Exception {
    String prefixToken = JwtProperties.TOKEN_PREFIX + createTestToken("test@test", "1234");

    mockMvc
        .perform(
            get("/test/none")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());
  }

  @Test
  @DisplayName("JWT인가필터 - JWT 토큰 만료")
  void doFilterInternalExpireToken() throws Exception {
    String testAccessToken =
        JwtProperties.TOKEN_PREFIX
            + JWT.create()
                .withSubject("test@test.com")
                .withExpiresAt(new Date(System.currentTimeMillis()))
                .withClaim(JwtProperties.USER_EMAIL, "test@test.com")
                .sign(Algorithm.HMAC256(JwtProperties.ACCESS_SECRET));

    assertThatExceptionOfType(TokenException.class)
        .isThrownBy(
            () -> {
              mockMvc
                  .perform(
                      get("/test/none")
                          .contentType(MediaType.APPLICATION_JSON)
                          .header(JwtProperties.HEADER_STRING, testAccessToken))
                  .andExpect(status().is4xxClientError())
                  .andDo(print());
            })
        .withMessage("엑세스 토큰이 만료됐거나 올바르지 않습니다.");
  }

  /**
   *
   *
   * <h1>권한검증</h1>
   */
  @Test
  @DisplayName("JWT인가필터 - JWT 토큰 ROLE_USER 권한 확인")
  void doFilterInternalUserAuth() throws Exception {
    String prefixToken = JwtProperties.TOKEN_PREFIX + createTestToken("test@test", "1234");

    mockMvc
        .perform(
            get("/test/user")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());
  }

  @Test
  @DisplayName("JWT인가필터 - JWT 토큰 ROLE_ADMIN 권한 확인")
  void doFilterInternalAdminAuth() throws Exception {
    String prefixToken = JwtProperties.TOKEN_PREFIX + createTestToken("test@test", "1234");

    mockMvc
        .perform(
            get("/test/admin")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

  @Test
  @DisplayName("JWT인가필터 - 사용자가 관리자 페이지 접근")
  void doFilterInternalAccessRoleAdminUrl() throws Exception {
    String prefixToken = JwtProperties.TOKEN_PREFIX + createTestToken("test@test", "1234");

    mockMvc
        .perform(
            get("/test/admin")
                .header(JwtProperties.HEADER_STRING, prefixToken)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }
}
