package com.limited.sales.filter;

import com.google.gson.Gson;
import com.limited.sales.exception.sub.LoginException;
import com.limited.sales.exception.sub.TokenException;
import com.limited.sales.principal.PrincipalDetails;
import com.limited.sales.redis.RedisService;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProperties;
import com.limited.sales.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 요청이 들어올 때 마다 새로운 쓰레드가 생성된다.
 * 만약 필터를 Bean 으로 등록한다면, 스코프가 싱글톤인데 쓰레드에 세이프 할까?
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final RedisService redisService;
    private final JwtProvider jwtProvider = new JwtProvider();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            final Gson gson = new Gson();
            final User user = gson.fromJson(new InputStreamReader(request.getInputStream()), User.class);

            final UsernamePasswordAuthenticationToken createToken =
                    new UsernamePasswordAuthenticationToken(user.getUserEmail(), user.getUserPassword());

            return authenticationManager.authenticate(createToken);
        } catch (AuthenticationException authenticationException){
            authenticationException.printStackTrace();
            throw new LoginException("인증 실패");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("인증 도중 예외 발생");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        try {
            final PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

            final String accessToken = jwtProvider.createAccessTokenMethod(principalDetails.getUser());
            final String refreshToken = jwtProvider.createRefreshTokenMethod(principalDetails.getUser());
            final String userEmail = principalDetails.getUser().getUserEmail();

            redisService.setValues(userEmail, refreshToken);
            response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
        } catch (TokenException e){
            e.printStackTrace();
            throw new TokenException("토큰 생성 도중 오류가 발생했습니다.");
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("알 수 없는 오류가 발생했습니다.");
        }
    }
}
