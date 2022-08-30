package com.limited.sales.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class JwtValidationUtilsTest {
  /**
   *
   *
   * <h1>엑세스토큰 테스트 라인</h1>
   */
  @Test
  @DisplayName("엑세스토큰 검증 - true 정상적인 토큰")
  void isAccessTokenValidSuccess() {
    String testAccessToken =
        JWT.create()
            .withSubject("test@test.com")
            .withExpiresAt(new Date(System.currentTimeMillis() + 60000 * 10))
            .withClaim(JwtProperties.USER_EMAIL, "test@test.com")
            .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));

    boolean isTestAccessTokenValid = JwtValidationUtils.isAccessTokenValid(testAccessToken);

    assertThat(isTestAccessTokenValid).isTrue();
  }

  @Test
  @DisplayName("엑세스토큰 검증 - false 클레임 없음")
  void noClaimAccessTokenValid() {
    String testAccessToken =
        JWT.create()
            .withSubject("test@test.com")
            .withExpiresAt(new Date(System.currentTimeMillis() + 60000 * 10))
            .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));

    boolean isTestAccessTokenValid = JwtValidationUtils.isAccessTokenValid(testAccessToken);

    assertThat(isTestAccessTokenValid).isFalse();
  }

  @Test
  @DisplayName("엑세스토큰 검증 - false Subject 없음")
  void noSubjectAccessTokenValid() {
    String testAccessToken =
        JWT.create()
            .withExpiresAt(new Date(System.currentTimeMillis() + 60000 * 10))
            .withClaim(JwtProperties.USER_EMAIL, "test@test.com")
            .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));

    boolean isTestAccessTokenValid = JwtValidationUtils.isAccessTokenValid(testAccessToken);

    assertThat(isTestAccessTokenValid).isFalse();
  }

  @Test
  @DisplayName("엑세스토큰 검증 - false Secret 값이 잘못된 토큰")
  void isAccessTokenValidFail() {
    String testAccessToken =
        JWT.create()
            .withSubject("test@test.com")
            .withExpiresAt(new Date(System.currentTimeMillis() + 60000 * 10))
            .withClaim(JwtProperties.USER_EMAIL, "test@test.com")
            .sign(Algorithm.HMAC512("TEST"));

    boolean isTestAccessTokenValid = JwtValidationUtils.isAccessTokenValid(testAccessToken);

    assertThat(isTestAccessTokenValid).isFalse();
  }

  @Test
  @DisplayName("엑세스토큰 검증 - false 만료된 토큰")
  void expiredAccessToken() {
    String testAccessToken =
        JWT.create()
            .withSubject("test@test.com")
            .withExpiresAt(new Date(System.currentTimeMillis()))
            .withClaim(JwtProperties.USER_EMAIL, "test@test.com")
            .sign(Algorithm.HMAC256(JwtProperties.ACCESS_SECRET));

    boolean isTestAccessTokenValid = JwtValidationUtils.isAccessTokenValid(testAccessToken);

    assertThat(isTestAccessTokenValid).isFalse();
  }

  @Test
  @DisplayName("엑세스토큰 검증 - false 공백")
  void emptyAccessToken() {
    String testAccessToken = "";

    boolean isTestAccessTokenValid = JwtValidationUtils.isAccessTokenValid(testAccessToken);

    assertThat(isTestAccessTokenValid).isFalse();
  }

  @Test
  @DisplayName("엑세스토큰 검증 - false NULL")
  void nullAccessToken() {
    String testAccessToken = null;

    boolean isTestAccessTokenValid = JwtValidationUtils.isAccessTokenValid(testAccessToken);

    assertThat(isTestAccessTokenValid).isFalse();
  }

  /**
   *
   *
   * <h1>리프레쉬 토큰 테스트 라인</h1>
   */
  @Test
  @DisplayName("리프레쉬토큰 검증 - true 정상적인 토큰")
  void isRefreshTokenValid() {
    String testRefreshToken =
        JWT.create()
            .withExpiresAt(new Date(System.currentTimeMillis() + 60000 * 10))
            .withClaim(JwtProperties.USER_EMAIL, "test@test.com")
            .sign(Algorithm.HMAC512(JwtProperties.REFRESH_SECRET));

    boolean isTestRefreshTokenValid = JwtValidationUtils.isRefreshTokenValid(testRefreshToken);

    assertThat(isTestRefreshTokenValid).isTrue();
  }

  @Test
  @DisplayName("리프레쉬토큰 검증 - false Secret 값이 잘못된 토큰")
  void isRefreshTokenValidFail() {
    String testRefreshToken =
        JWT.create()
            .withExpiresAt(new Date(System.currentTimeMillis() + 60000 * 10))
            .withClaim(JwtProperties.USER_EMAIL, "test@test.com")
            .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));

    boolean isTestRefreshTokenValid = JwtValidationUtils.isRefreshTokenValid(testRefreshToken);

    assertThat(isTestRefreshTokenValid).isFalse();
  }

  @Test
  @DisplayName("리프레쉬토큰 검증 - false NULL")
  void nullRefreshTokenValid() {
    String testRefreshToken = null;

    boolean isTestRefreshTokenValid = JwtValidationUtils.isRefreshTokenValid(testRefreshToken);

    assertThat(isTestRefreshTokenValid).isFalse();
  }

  @Test
  @DisplayName("리프레쉬토큰 검증 - false 공백")
  void emptyRefreshTokenValid() {
    String testRefreshToken = "";

    boolean isTestRefreshTokenValid = JwtValidationUtils.isRefreshTokenValid(testRefreshToken);

    assertThat(isTestRefreshTokenValid).isFalse();
  }

  @Test
  @DisplayName("리프레쉬토큰 검증 - false 만료된 토큰")
  void expireRefreshTokenValid() {
    String testRefreshToken =
        JWT.create()
            .withExpiresAt(new Date(System.currentTimeMillis()))
            .withClaim(JwtProperties.USER_EMAIL, "test@test.com")
            .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));

    boolean isTestRefreshTokenValid = JwtValidationUtils.isRefreshTokenValid(testRefreshToken);

    assertThat(isTestRefreshTokenValid).isFalse();
  }

  @Test
  @DisplayName("리프레쉬토큰 검증 - false 클레임 없음")
  void noClaimRefreshTokenValid() {
    String testRefreshToken =
        JWT.create()
            .withExpiresAt(new Date(System.currentTimeMillis() + 60000 * 10))
            .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));

    boolean isTestRefreshTokenValid = JwtValidationUtils.isRefreshTokenValid(testRefreshToken);

    assertThat(isTestRefreshTokenValid).isFalse();
  }

  /**
   *
   *
   * <h1>Bearer 체크 라인</h1>
   */
  @Test
  @DisplayName("토큰존재 유무 확인 - true 정상적인 토큰")
  void hasValidJwtToken() {
    String testAccessToken =
        JwtProperties.TOKEN_PREFIX
            + JWT.create()
                .withSubject("test@test.com")
                .withExpiresAt(new Date(System.currentTimeMillis() + 60000 * 10))
                .withClaim(JwtProperties.USER_EMAIL, "test@test.com")
                .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));

    boolean testJwtToken = JwtValidationUtils.hasValidJwtTokenNull(testAccessToken);

    assertThat(testJwtToken).isTrue();
  }

  @Test
  @DisplayName("토큰존재 유무 확인 - false Prefix 가 없는 토큰")
  void hasValidNotPrefixJwtToken() {
    String testAccessToken =
        JWT.create()
            .withSubject("test@test.com")
            .withExpiresAt(new Date(System.currentTimeMillis() + 60000 * 10))
            .withClaim(JwtProperties.USER_EMAIL, "test@test.com")
            .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));

    boolean testJwtToken = JwtValidationUtils.hasValidJwtTokenNull(testAccessToken);

    assertThat(testJwtToken).isFalse();
  }

  @Test
  @DisplayName("토큰존재 유무 확인 - false 토큰 공백")
  void hasValidJwtTokenEmpty() {
    String testAccessToken = "";

    boolean testJwtToken = JwtValidationUtils.hasValidJwtTokenNull(testAccessToken);

    assertThat(testJwtToken).isFalse();
  }

  @Test
  @DisplayName("토큰존재 유무 확인 - false NULL")
  void hasValidJwtTokenNull() {
    String testAccessToken = null;

    boolean testJwtToken = JwtValidationUtils.hasValidJwtTokenNull(testAccessToken);

    assertThat(testJwtToken).isFalse();
  }
}
