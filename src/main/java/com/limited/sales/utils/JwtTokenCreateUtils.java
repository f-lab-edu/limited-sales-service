package com.limited.sales.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.limited.sales.config.jwt.JwtProperties;
import com.limited.sales.user.vo.User;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class JwtTokenCreateUtils {
    public String createAccessTokenMethod(final @NotNull User user) {
        return JWT.create()
                .withSubject(user.getUserEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("userEmail", user.getUserEmail())
                .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));
    }

    public String createRefreshTokenMethod(final @NotNull User user) {
        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME * 10L))
                .withClaim("userEmail", user.getUserEmail())
                .sign(Algorithm.HMAC512(JwtProperties.REFRESH_SECRET));
    }
}
