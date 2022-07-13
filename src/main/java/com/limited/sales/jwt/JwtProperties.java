package com.limited.sales.jwt;

public class JwtProperties {
  public static final String SECRET = "limited"; // 서버만 알고있는 비밀값
  public static final int EXPIRATION_TIME_MS = 60000 * 10; // 10분
  public static final int REFRESH_EXPIRATION_TIME_MS = 60000 * 60 * 24; // 24시간
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String BLACKLIST_TOKEN_PREFIX = "BlackList ";
  public static final String HEADER_STRING = "Authorization";
}
