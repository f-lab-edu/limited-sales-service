package com.limited.sales.jwt;

public interface JwtProperties {
    String SECRET = "limited"; //서버만 알고있는 비밀값
    int EXPIRATION_TIME = 60000*10; //10분
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
