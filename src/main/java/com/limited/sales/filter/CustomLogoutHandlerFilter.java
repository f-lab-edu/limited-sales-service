package com.limited.sales.filter;

import com.limited.sales.token.TokenService;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProperties;
import com.limited.sales.utils.JwtUtils;
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

  @Override
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    final String header = request.getHeader(JwtProperties.HEADER_STRING);
    final String prefixToken = JwtUtils.replaceTokenPrefix(header);
    final String userEmail = JwtUtils.getClaim(prefixToken, JwtProperties.USER_EMAIL).asString();
    final User user = User.builder().userEmail(userEmail).build();

    tokenService.deleteRefreshToken(user);
    tokenService.blacklistAccessToken(user, header);
  }
}
