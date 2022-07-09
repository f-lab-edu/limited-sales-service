package com.limited.sales.utils;

import com.auth0.jwt.algorithms.Algorithm;

import javax.validation.constraints.NotNull;

import static com.auth0.jwt.JWT.require;

public final class JwtValidation {
    private final JwtProvider jwtProvider = new JwtProvider();
    public boolean isValidationAccessTokenCheck(final @NotNull String token){
        try {
            final String prefixToken = jwtProvider.replaceTokenPrefix(token);
            require(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET))
                    .build()
                    .verify(prefixToken);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isValidationRefreshTokenCheck(final @NotNull String token){
        try {
            require(Algorithm.HMAC512(JwtProperties.REFRESH_SECRET))
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isValidationAuthorizationCheck(final @NotNull String token){
        final boolean result;

        if (token == null)  result = true; // 토큰이 없을 경우 true
        else                result = !token.startsWith(JwtProperties.TOKEN_PREFIX); // 토큰이 있을 경우 false

        return result;
    }
}
