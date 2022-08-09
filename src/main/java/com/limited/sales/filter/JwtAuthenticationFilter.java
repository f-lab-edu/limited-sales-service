package com.limited.sales.filter;

import com.google.gson.JsonSyntaxException;
import com.limited.sales.config.LazyObjectHolder;
import com.limited.sales.exception.sub.LoginException;
import com.limited.sales.exception.sub.NoValidUserException;
import com.limited.sales.exception.sub.TokenException;
import com.limited.sales.principal.PrincipalDetails;
import com.limited.sales.redis.RedisService;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProperties;
import com.limited.sales.utils.JwtUtils;
import io.lettuce.core.RedisConnectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;
  private final RedisService redisService;

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) {
    try {
      final User user =
          Optional.ofNullable(
                  LazyObjectHolder.getGson()
                      .fromJson(new InputStreamReader(request.getInputStream()), User.class))
              .orElse(new User());

      final UsernamePasswordAuthenticationToken createdToken =
          new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

      return authenticationManager.authenticate(createdToken);
    } catch (NullPointerException e) {
      log.error(e.getMessage());
      throw new RuntimeException("사용자 데이터가 정상적이지 않습니다.");
    } catch (JsonSyntaxException e) {
      log.error(e.getMessage());
      throw new JsonSyntaxException("Json 파싱 에러");
    } catch (NoValidUserException e) {
      log.error(e.getMessage());
      throw new NoValidUserException("계정이 존재하지 않습니다.");
    } catch (AuthenticationException e) {
      log.error(e.getMessage());
      throw new LoginException("인증 실패");
    } catch (Exception e) {
      log.error(e.getMessage());
      e.printStackTrace();
      throw new RuntimeException("알 수 없는 오류로 로그인 실패");
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) {
    try {
      final PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

      final String accessToken = JwtUtils.createAccessToken(principalDetails.getUser());
      final String refreshToken = JwtUtils.createRefreshToken(principalDetails.getUser());
      final String userEmail = principalDetails.getUser().getEmail();

      redisService.setValue(userEmail, refreshToken);
      response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
    } catch (TokenException e) {
      e.printStackTrace();
      throw new TokenException("토큰 생성 도중 오류가 발생했습니다.");
    } catch (RedisConnectionFailureException e) {
      log.error(e.getMessage());
      throw new RedisConnectionFailureException("Redis 연결이 실패했습니다.");
    } catch (RedisConnectionException e) {
      log.error(e.getMessage());
      throw new RedisConnectionException("Redis 연결에 문제가 생겼습니다.");
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException("알 수 없는 오류가 발생했습니다.");
    }
  }
}
