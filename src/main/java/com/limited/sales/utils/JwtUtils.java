package com.limited.sales.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.limited.sales.user.vo.User;

import javax.validation.constraints.NotNull;
import java.util.Date;

public final class JwtUtils {
  public static String createAccessToken(final @NotNull User user) {
    return JWT.create()
        .withSubject(user.getUserEmail())
        .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME_MS))
        .withClaim(JwtProperties.USER_EMAIL, user.getUserEmail())
        .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));
  }

  public static String createRefreshToken(final @NotNull User user) {
    return JWT.create()
        .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME_MS))
        .withClaim(JwtProperties.USER_EMAIL, user.getUserEmail())
        .sign(Algorithm.HMAC512(JwtProperties.REFRESH_SECRET));
  }

  public static Claim getClaim(final @NotNull String token, final @NotNull String claim) {
    return JWT.require(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET))
        .build()
        .verify(token)
        .getClaim(claim);
  }

  public static String replaceTokenPrefix(final @NotNull String header) {
    return header.replace(JwtProperties.TOKEN_PREFIX, "");
  }
}
