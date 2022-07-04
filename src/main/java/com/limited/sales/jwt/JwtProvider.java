package com.limited.sales.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.limited.sales.auth.PrincipalDetails;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtProvider {

    public String getToken(PrincipalDetails principalDetails, String subject) {

        String token = null;

        if(subject.equals("accessToken")) {
            //RSA방식은 아니고 Hash 암호 방식
            token = JWT.create()
                    .withSubject(subject)
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                    .withClaim("userEmail", principalDetails.getUser().getUserEmail())
                    .withClaim("userPassword", principalDetails.getUser().getUserPassword()) //비공개 클레임, 넣고싶은 키+벨류값 막 넣을 수 있다.
                    .sign(Algorithm.HMAC512(JwtProperties.SECRET)); //서버만 아는 시크릿 코드값.
        } else {
            token = JWT.create()
                    .withSubject(subject)
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME))
                    .withClaim("userEmail", principalDetails.getUser().getUserEmail())
                    .withClaim("userPassword", principalDetails.getUser().getUserPassword()) //비공개 클레임, 넣고싶은 키+벨류값 막 넣을 수 있다.
                    .sign(Algorithm.HMAC512(JwtProperties.SECRET)); //서버만 아는 시크릿 코드값.
        }
        return token;
    }



}
