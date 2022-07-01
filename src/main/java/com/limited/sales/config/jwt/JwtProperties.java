package com.limited.sales.config.jwt;

public interface JwtProperties {
    String ACCESS_SECRET = "limited";
    String REFRESH_SECRET = "sales";
    int EXPIRATION_TIME = 864000000; // 10일
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String ACCESS_TOKEN = "AccessToken";
    String REFRESH_TOKEN = "RefreshToken";
}
