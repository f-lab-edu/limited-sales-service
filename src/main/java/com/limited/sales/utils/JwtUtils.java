package com.limited.sales.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.limited.sales.exception.sub.BadRequestException;
import com.limited.sales.exception.sub.NotAllowedClaimException;
import com.limited.sales.user.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Validated
public final class JwtUtils {
  public static String createAccessToken(@NotNull(message = "사용자 정보가 존재하지 않습니다.") final User user) {
    if (StringUtils.isBlank(user.getEmail())) {
      throw new BadRequestException("사용자 이메일 존재하지 않습니다.");
    }

    return JWT.create()
        .withSubject(user.getEmail())
        .withExpiresAt(
            new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME_MS))
        .withClaim(JwtProperties.USER_EMAIL, user.getEmail())
        .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));
  }

  public static String createRefreshToken(
      @NotNull(message = "사용자 정보가 존재하지 않습니다.") final User user) {
    if (StringUtils.isBlank(user.getEmail())) {
      throw new BadRequestException("사용자 이메일 값이 존재하지 않습니다.");
    }

    return JWT.create()
        .withExpiresAt(
            new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME_MS))
        .withClaim(JwtProperties.USER_EMAIL, user.getEmail())
        .sign(Algorithm.HMAC512(JwtProperties.REFRESH_SECRET));
  }

  public static Claim getClaim(
      @NotBlank(message = "토큰 값이 존재하지 않습니다.") final String token,
      @NotBlank(message = "Claim 값이 존재하지 않습니다.") final String claim) {

    final Claim claimData =
        JWT.require(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET))
            .build()
            .verify(token)
            .getClaim(claim);

    if (claimData.isNull() || StringUtils.isBlank(claimData.asString())) {
      throw new NotAllowedClaimException("Claim 이 존재하지 않습니다.");
    }

    return claimData;
  }

  public static String replaceTokenPrefix(
      @NotBlank(message = "토큰이 존재하지 않습니다.") final String header) {

    return header.replace(JwtProperties.TOKEN_PREFIX, "");
  }
}
