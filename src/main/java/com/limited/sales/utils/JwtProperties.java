package com.limited.sales.utils;

public class JwtProperties {
  public static final String ACCESS_SECRET = "limited";
  public static final String REFRESH_SECRET = "sales";
  public static final int ACCESS_EXPIRATION_TIME_MS = 60000 * 10; // 10 분
  public static final int REFRESH_EXPIRATION_TIME_MS = 60000 * 60 * 24; // 24 시간
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER_STRING = "Authorization";
  public static final String USER_EMAIL = "userEmail";
  public static final String BLACKLIST_POSTFIX = "_black";
}
