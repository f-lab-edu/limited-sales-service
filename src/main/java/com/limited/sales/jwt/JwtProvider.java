package com.limited.sales.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.limited.sales.user.vo.User;

import java.util.Date;

public class JwtProvider {

  public String getToken(User user, String subject) {
    String token = null;
    token =
        JWT.create()
            .withSubject(subject)
            .withExpiresAt(
                new Date(
                    System.currentTimeMillis()
                        + (subject.equals("accessToken")
                            ? JwtProperties.EXPIRATION_TIME_MS
                            : JwtProperties.REFRESH_EXPIRATION_TIME_MS)))
            .withClaim("userEmail", user.getUserEmail())
            .withClaim("userPassword", user.getUserPassword()) // 비공개 클레임, 넣고싶은 키+벨류값 막 넣을 수 있다.
            .sign(Algorithm.HMAC512(JwtProperties.SECRET)); // 서버만 아는 시크릿 코드값.
    return token;
  }
}
