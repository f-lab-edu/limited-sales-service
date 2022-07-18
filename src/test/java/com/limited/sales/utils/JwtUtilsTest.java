package com.limited.sales.utils;

import com.auth0.jwt.interfaces.Claim;
import com.limited.sales.exception.sub.BadRequestException;
import com.limited.sales.exception.sub.NoValidUserException;
import com.limited.sales.user.vo.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class JwtUtilsTest {
  /**
   *
   *
   * <h1>엑세스 토큰 생성</h1>
   */
  @Test
  @DisplayName("엑세스 토큰 생성 - 정상적으로 토큰 생성")
  void createAccessToken() {
    User testUser = User.builder().userEmail("test@test.com").build();
    String accessToken = JwtUtils.createAccessToken(testUser);
    boolean result = JwtValidationUtils.isAccessTokenValid(accessToken);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("엑세스 토큰 생성 - email 공백")
  void createAccessTokenUserEmailEmpty() {
    User testUser = User.builder().userEmail("").build();

    assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(
            () -> {
              JwtUtils.createAccessToken(testUser);
            })
        .withMessage("사용자 이메일 값이 존재하지 않습니다.");
  }

  @Test
  @DisplayName("엑세스 토큰 생성 - email NULL")
  void createAccessTokenUserEmailNull() {
    User testUser = User.builder().userEmail(null).build();

    assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(
            () -> {
              JwtUtils.createAccessToken(testUser);
            })
        .withMessage("사용자 이메일 값이 존재하지 않습니다.");
  }

  @Test
  @DisplayName("엑세스 토큰 생성 - User Null")
  void createAccessTokenUserNull() {
    User testUser = null;

    assertThatExceptionOfType(NoValidUserException.class)
        .isThrownBy(
            () -> {
              JwtUtils.createAccessToken(testUser);
            })
        .withMessage("사용자 정보가 없습니다.");
  }

  @Test
  @DisplayName("엑세스 토큰 생성 - 이메일 형식이 아닐 경우")
  void createAccessTokenUserNotEmailFormat() {
    User testUser = User.builder().userEmail("test").build();

    assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(
            () -> {
              JwtUtils.createAccessToken(testUser);
            })
        .withMessage("이메일 형식이 아닙니다.");
  }

  /**
   *
   *
   * <h1>리프레쉬 토큰 생성</h1>
   */
  @Test
  @DisplayName("리프레쉬 토큰 생성 - 정상적으로 토큰 생성")
  void createRefreshToken() {
    User testUser = User.builder().userEmail("test@test.com").build();
    String accessToken = JwtUtils.createRefreshToken(testUser);
    boolean result = JwtValidationUtils.isRefreshTokenValid(accessToken);

    assertThat(result).isTrue();
  }

  /**
   *
   *
   * <h1>클레임 반환</h1>
   */
  @Test
  @DisplayName("클레임 반환 - 정상적으로 반환")
  void getClaim() {
    User testUser = User.builder().userEmail("test@test.com").build();
    String accessToken = JwtUtils.createAccessToken(testUser);
    Claim claim = JwtUtils.getClaim(accessToken, JwtProperties.USER_EMAIL);

    assertThat(claim.asString()).isEqualTo("test@test.com");
  }

  /**
   *
   *
   * <h1>토큰 Prefix 제거</h1>
   */
  @Test
  @DisplayName("Prefix 제거 - 정상적으로 제거 후 토큰 반환")
  void replaceTokenPrefix() {
    User testUser = User.builder().userEmail("test@test.com").build();
    String prefixAccessToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(testUser);
    String accessToken = JwtUtils.replaceTokenPrefix(prefixAccessToken);

    assertThat(accessToken).isNotNull().isNotEqualTo(JwtProperties.TOKEN_PREFIX);
  }
}
