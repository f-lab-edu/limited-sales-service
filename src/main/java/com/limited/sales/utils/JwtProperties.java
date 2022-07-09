package com.limited.sales.utils;

public interface JwtProperties {
    String ACCESS_SECRET = "limited";
    String REFRESH_SECRET = "sales";
    int EXPIRATION_TIME = 864000000; // 10일
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
