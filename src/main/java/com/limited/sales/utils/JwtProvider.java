package com.limited.sales.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.limited.sales.user.vo.User;

import javax.validation.constraints.NotNull;
import java.util.Date;

public final class JwtProvider {
  public String createAccessTokenMethod(final @NotNull User user) {
    return JWT.create()
        .withSubject(user.getUserEmail())
        .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
        .withClaim("userEmail", user.getUserEmail())
        .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));
  }

  public String createRefreshTokenMethod(final @NotNull User user) {
    return JWT.create()
        .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME))
        .withClaim("userEmail", user.getUserEmail())
        .sign(Algorithm.HMAC512(JwtProperties.REFRESH_SECRET));
  }

  public Claim getClaim(final @NotNull String token, final @NotNull String claim) {
    return JWT.require(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET))
        .build()
        .verify(token)
        .getClaim(claim);
  }

  public String replaceTokenPrefix(final @NotNull String header) {
    return header.replace(JwtProperties.TOKEN_PREFIX, "");
  }
}
