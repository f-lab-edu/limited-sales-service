package com.limited.sales.jwt;

import com.limited.sales.error.exception.user.InvalidAuthenticationException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtTokenFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    // 여기서는 로그인 시도할 때, 토큰 값을 limited로 잘들고 오는지 검증하기 위해
    // 1차적으로 한번 걸러주는 역할을 한다.
    if (req.getMethod().equals("POST")) {
      String headerAuth = req.getHeader(JwtProperties.HEADER_STRING);

      if (headerAuth.equals(JwtProperties.SECRET)) {
        chain.doFilter(req, res);
      } else {
        throw new InvalidAuthenticationException("사용자 인증이 실패 되었습니다.");
      }
    } else {
      chain.doFilter(req, res);
    }
  }
}
