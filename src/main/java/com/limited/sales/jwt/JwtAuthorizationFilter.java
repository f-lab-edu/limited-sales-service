package com.limited.sales.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.limited.sales.auth.PrincipalDetails;
import com.limited.sales.error.exception.UserEmailNullException;
import com.limited.sales.redis.RedisService;
import com.limited.sales.user.vo.User;
import com.limited.sales.user.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  private UserMapper userMapper;

  private RedisService redisService;

  public JwtAuthorizationFilter(
      AuthenticationManager authenticationManager,
      UserMapper userMapper,
      RedisService redisService) {
    super(authenticationManager);
    this.userMapper = userMapper;
    this.redisService = redisService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);

    // header가 있는지 확인.
    if (jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }

    // JWT access 토큰을 검증해서 정상적인 사용자인지 확인을 한다.
    String jwtToken = jwtHeader.replace(JwtProperties.TOKEN_PREFIX, "");
    // 만료된 토큰인지 먼저 확인을 한다.
    String blacklistToken = redisService.getValues(JwtProperties.BLACKLIST_TOKEN_PREFIX + jwtToken);

    if (blacklistToken == null) {

      // 서명이 정상적으로 되면, userEmail을 가져온다.
      String userEmail = getClaim(jwtToken);

      // 이 userEmail을 통해서 레디스에 일치하는 refreshToken이 존재하는지 유효성을 검증한다.
      String redisRT = redisService.getValues(userEmail);

      // 서명이 정상적으로 됨. (= refresh 토큰 정보가 유효하다면 ) (= 만약 refresh 토큰이 만료된 상태라면, 미리 해둔 expired time 설정을
      // 통해 redis 저장소에서 토큰은 자동으로 삭제된다.)
      if (redisRT != null) {

        Optional<User> userChk = Optional.of(userMapper.findByUserEmail(userEmail));
        User userEntity = userChk.orElseThrow(() -> new UserEmailNullException("존재하지 않는 사용자입니다."));

        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

        // 로그인을 하지 않더라도, 강제로 객체를 만들수있다.
        // 지금은 로그인을 따로 한게 아니라, 토큰을 통해 서명이 잘 검증되어서, userEmail이 있으면 authentication 객체를 만드는것이다.
        Authentication authentication =
            new UsernamePasswordAuthenticationToken(
                principalDetails, null, principalDetails.getAuthorities());

        // 시큐리티를 저장할수있는 세션공간을 찾아서, (강제로 시큐리티의 세션에 접근하여) authentication 객체를 저장.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);

      } else {
        throw new RuntimeException("refresh 토큰 정보가 유효하지 않습니다.");
      }
    } else {
      throw new RuntimeException("access 토큰 정보가 유효하지 않습니다.");
    }
  }

  private String getClaim(String jwtToken) {
    return JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
        .build()
        .verify(jwtToken)
        .getClaim("userEmail")
        .asString();
  }
}
