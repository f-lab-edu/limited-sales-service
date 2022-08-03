package com.limited.sales.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.limited.sales.exception.sub.BadRequestException;
import com.limited.sales.exception.sub.NoValidUserException;
import com.limited.sales.exception.sub.NotAllowedClaimException;
import com.limited.sales.user.vo.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

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
    User testUser = User.builder().email("test@test.com").build();
    String accessToken = JwtUtils.createAccessToken(testUser);
    boolean result = JwtValidationUtils.isAccessTokenValid(accessToken);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("엑세스 토큰 생성 - email 공백")
  void createAccessTokenUserEmailEmpty() {
    User testUser = User.builder().email("").build();

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
    User testUser = User.builder().email(null).build();

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
    User testUser = User.builder().email("test").build();

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
    User testUser = User.builder().email("test@test.com").build();
    String accessToken = JwtUtils.createRefreshToken(testUser);
    boolean result = JwtValidationUtils.isRefreshTokenValid(accessToken);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("리프레쉬 토큰 생성 - email 공백")
  void createRefreshTokenUserEmailEmpty() {
    User testUser = User.builder().email("").build();

    assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(
            () -> {
              JwtUtils.createRefreshToken(testUser);
            })
        .withMessage("사용자 이메일 값이 존재하지 않습니다.");
  }

  @Test
  @DisplayName("리프레쉬 토큰 생성 - email NULL")
  void createRefreshTokenUserEmailNull() {
    User testUser = User.builder().email(null).build();

    assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(
            () -> {
              JwtUtils.createRefreshToken(testUser);
            })
        .withMessage("사용자 이메일 값이 존재하지 않습니다.");
  }

  @Test
  @DisplayName("리프레쉬 토큰 생성 - User Null")
  void createRefreshTokenUserNull() {
    User testUser = null;

    assertThatExceptionOfType(NoValidUserException.class)
        .isThrownBy(
            () -> {
              JwtUtils.createRefreshToken(testUser);
            })
        .withMessage("사용자 정보가 없습니다.");
  }

  @Test
  @DisplayName("리프레쉬 토큰 생성 - 이메일 형식이 아닐 경우")
  void createRefreshTokenUserNotEmailFormat() {
    User testUser = User.builder().email("test").build();

    assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(
            () -> {
              JwtUtils.createRefreshToken(testUser);
            })
        .withMessage("이메일 형식이 아닙니다.");
  }

  /**
   *
   *
   * <h1>클레임 반환</h1>
   */
  @Test
  @DisplayName("클레임 반환 - 정상적으로 반환")
  void getClaim() {
    User testUser = User.builder().email("test@test.com").build();
    String accessToken = JwtUtils.createAccessToken(testUser);
    Claim claim = JwtUtils.getClaim(accessToken, JwtProperties.USER_EMAIL);

    assertThat(claim.asString()).isEqualTo("test@test.com");
  }

  @Test
  @DisplayName("클레임 반환 - 이메일 공백")
  void getClaimEmptyEmail() {
    User testUser = User.builder().email("test@test").build();

    String accessToken =
        JWT.create()
            .withSubject(testUser.getEmail())
            .withExpiresAt(
                new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME_MS))
            .withClaim(JwtProperties.USER_EMAIL, testUser.getEmail())
            .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));

    assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(
            () -> {
              JwtUtils.getClaim(accessToken, "");
            })
        .withMessage("Claim 값이 존재하지 않습니다.");
  }

  @Test
  @DisplayName("클레임 반환 - 이메일 Null")
  void getClaimNullEmail() {
    User testUser = User.builder().email("test@test").build();
    String accessToken =
        JWT.create()
            .withSubject(testUser.getEmail())
            .withExpiresAt(
                new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME_MS))
            .withClaim(JwtProperties.USER_EMAIL, testUser.getEmail())
            .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));

    assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(
            () -> {
              JwtUtils.getClaim(accessToken, null);
            })
        .withMessage("Claim 값이 존재하지 않습니다.");
  }

  @Test
  @DisplayName("클레임 반환 - 토큰 공백")
  void getClaimEmptyToken() {
    assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(
            () -> {
              JwtUtils.getClaim("", JwtProperties.USER_EMAIL);
            })
        .withMessage("token 값이 존재하지 않습니다.");
  }

  @Test
  @DisplayName("클레임 반환 - 토큰 Null")
  void getClaimNullToken() {
    assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(
            () -> {
              JwtUtils.getClaim(null, JwtProperties.USER_EMAIL);
            })
        .withMessage("token 값이 존재하지 않습니다.");
  }

  @Test
  @DisplayName("클레임 반환 - 토큰 안에 이메일 공백")
  void getClaimDecodedEmptyEmail() {
    User testUser = User.builder().email("").build();

    String accessToken =
        JWT.create()
            .withSubject(testUser.getEmail())
            .withExpiresAt(
                new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME_MS))
            .withClaim(JwtProperties.USER_EMAIL, testUser.getEmail())
            .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));

    assertThatExceptionOfType(NotAllowedClaimException.class)
        .isThrownBy(
            () -> {
              JwtUtils.getClaim(accessToken, JwtProperties.USER_EMAIL);
            })
        .withMessage("검증되지 않은 Claim 이거나 Claim 가 존재하지 않습니다.");
  }

  @Test
  @DisplayName("클레임 반환 - 토큰 안에 이메일 Null")
  void getClaimDecodedNullEmail() {
    User testUser = User.builder().email(null).build();
    String accessToken =
        JWT.create()
            .withSubject(testUser.getEmail())
            .withExpiresAt(
                new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME_MS))
            .withClaim(JwtProperties.USER_EMAIL, testUser.getEmail())
            .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));

    assertThatExceptionOfType(NotAllowedClaimException.class)
        .isThrownBy(
            () -> {
              JwtUtils.getClaim(accessToken, JwtProperties.USER_EMAIL);
            })
        .withMessage("검증되지 않은 Claim 이거나 Claim 가 존재하지 않습니다.");
  }

  /**
   *
   *
   * <h1>토큰 Prefix 제거</h1>
   */
  @Test
  @DisplayName("Prefix 제거 - 정상적으로 제거 후 토큰 반환")
  void replaceTokenPrefix() {
    User testUser = User.builder().email("test@test.com").build();
    String prefixAccessToken = JwtProperties.TOKEN_PREFIX + JwtUtils.createAccessToken(testUser);
    String accessToken = JwtUtils.replaceTokenPrefix(prefixAccessToken);

    assertThat(accessToken).isNotNull().isNotEqualTo(JwtProperties.TOKEN_PREFIX);
  }

  @Test
  @DisplayName("Prefix 제거 - 토큰 값 null")
  void replaceTokenPrefixParameterNull() {
    assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(
            () -> {
              String accessToken = JwtUtils.replaceTokenPrefix(null);
            })
        .withMessage("토큰이 비었거나 존재하지 않습니다.");
  }

  @Test
  @DisplayName("Prefix 제거 - 토큰 값 empty")
  void replaceTokenPrefixParameterEmpty() {
    assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(
            () -> {
              String accessToken = JwtUtils.replaceTokenPrefix("");
            })
        .withMessage("토큰이 비었거나 존재하지 않습니다.");
  }
}
