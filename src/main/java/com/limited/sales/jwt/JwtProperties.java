package com.limited.sales.jwt;

public interface JwtProperties {
    String SECRET = "limited"; //서버만 알고있는 비밀값
    int EXPIRATION_TIME = 60000*30; //10분
    int REFRESH_EXPIRATION_TIME = 60000*60*24; //24시간
    String TOKEN_PREFIX = "Bearer ";
    String BLACKLIST_TOKEN_PREFIX = "BlackList ";
    String HEADER_STRING = "Authorization";
}
