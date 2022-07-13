package com.limited.sales.filter;

import com.limited.sales.token.TokenService;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProperties;
import com.limited.sales.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutHandlerFilter implements LogoutHandler {
  private final TokenService tokenService;
  private final JwtProvider jwtProvider = new JwtProvider();

  @Override
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    final String header = request.getHeader(JwtProperties.HEADER_STRING);
    final String prefixToken = jwtProvider.replaceTokenPrefix(header);
    final String userEmail = jwtProvider.getClaim(prefixToken, "userEmail").asString();
    final User user = User.builder().userEmail(userEmail).build();

    tokenService.refreshTokenDelete(user);
    tokenService.accessTokenBlack(user, header);
  }
}
