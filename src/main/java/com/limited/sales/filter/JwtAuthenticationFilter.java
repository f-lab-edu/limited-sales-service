package com.limited.sales.filter;

import com.google.gson.JsonSyntaxException;
import com.limited.sales.config.GsonSingleton;
import com.limited.sales.exception.sub.BadRequestException;
import com.limited.sales.exception.sub.LoginFailException;
import com.limited.sales.exception.sub.TokenException;
import com.limited.sales.principal.PrincipalDetails;
import com.limited.sales.redis.RedisService;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProperties;
import com.limited.sales.utils.JwtUtils;
import io.lettuce.core.RedisConnectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
                  GsonSingleton.getGson()
                      .fromJson(new InputStreamReader(request.getInputStream()), User.class))
              .orElseThrow(
                  () -> {
                    throw new LoginFailException("계정정보가 존재하지 않습니다. 다시 로그인 해주세요.");
                  });

      if (StringUtils.isBlank(user.getEmail())) {
        throw new LoginFailException("이메일이 존재하지 않습니다.");
      }

      if(StringUtils.isBlank(user.getPassword())){
        throw new LoginFailException("비밀번호가 존재하지 않습니다.");
      }

      final UsernamePasswordAuthenticationToken createdToken =
          new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

      return authenticationManager.authenticate(createdToken);
    } catch (JsonSyntaxException | IOException e) {
      log.error(e.getMessage());
      e.printStackTrace();
      throw new JsonSyntaxException("Json 파싱 에러");
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
    } catch (RedisConnectionFailureException | RedisConnectionException e) {
      log.error(e.getMessage());
      throw new RedisConnectionFailureException("Redis 연결이 실패했습니다.");
    }
  }
}
