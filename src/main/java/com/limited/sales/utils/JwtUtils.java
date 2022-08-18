package com.limited.sales.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.limited.sales.exception.sub.BadRequestException;
import com.limited.sales.exception.sub.NoValidUserException;
import com.limited.sales.exception.sub.NotAllowedClaimException;
import com.limited.sales.user.vo.User;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;

public final class JwtUtils {
  public static String createAccessToken(final @NotNull User user) {
    userDataValidation(user);

    return JWT.create()
        .withSubject(user.getEmail())
        .withExpiresAt(
            new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME_MS))
        .withClaim(JwtProperties.USER_EMAIL, user.getEmail())
        .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));
  }

  public static String createRefreshToken(final @NotNull User user) {
    userDataValidation(user);

    return JWT.create()
        .withExpiresAt(
            new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME_MS))
        .withClaim(JwtProperties.USER_EMAIL, user.getEmail())
        .sign(Algorithm.HMAC512(JwtProperties.REFRESH_SECRET));
  }

  public static Claim getClaim(final @NotNull String token, final @NotNull String claim) {
    claimParameterEmptyNullValidation(token, claim);

    Claim claimData =
        JWT.require(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET))
            .build()
            .verify(token)
            .getClaim(claim);

    claimDataValidation(claimData);

    return claimData;
  }

  public static String replaceTokenPrefix(final @NotNull String header) {
    if (StringUtils.isBlank(header)) {
      throw new BadRequestException("토큰이 비었거나 존재하지 않습니다.");
    }

    return header.replace(JwtProperties.TOKEN_PREFIX, "");
  }

  private static void claimDataValidation(Claim claimData) {
    if (claimData.isNull() || claimData.asString().equals("")) {
      throw new NotAllowedClaimException("검증되지 않은 Claim 이거나 Claim 가 존재하지 않습니다.");
    }
  }

  private static void claimParameterEmptyNullValidation(String token, String claim) {
    if (StringUtils.isBlank(claim)) {
      throw new BadRequestException("Claim 값이 존재하지 않습니다.");
    }

    if (StringUtils.isBlank(token)) {
      throw new BadRequestException("token 값이 존재하지 않습니다.");
    }
  }

  private static void userDataValidation(User user) {
    Optional.ofNullable(user)
        .orElseThrow(
            () -> {
              throw new NoValidUserException("사용자 정보가 없습니다.");
            });

    if (StringUtils.isBlank(user.getEmail())) {
      throw new BadRequestException("사용자 이메일 값이 존재하지 않습니다.");
    }
  }
}
