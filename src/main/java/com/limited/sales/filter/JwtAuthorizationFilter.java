package com.limited.sales.filter;

import com.limited.sales.exception.sub.TokenException;
import com.limited.sales.principal.PrincipalDetails;
import com.limited.sales.redis.RedisService;
import com.limited.sales.user.UserService;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProperties;
import com.limited.sales.utils.JwtUtils;
import com.limited.sales.utils.JwtValidationUtils;
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
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Slf4j
public final class JwtAuthorizationFilter extends BasicAuthenticationFilter {
  private final UserService userService;
  private final RedisService redisService;

  public JwtAuthorizationFilter(
      AuthenticationManager authenticationManager,
      UserService userService,
      RedisService redisService) {
    super(authenticationManager);
    this.userService = userService;
    this.redisService = redisService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    final String header = request.getHeader(JwtProperties.HEADER_STRING);

    if (!JwtValidationUtils.hasValidJwtToken(header)) {
      chain.doFilter(request, response);
      return;
    }
    if (!JwtValidationUtils.isAccessTokenValid(header)) {
      throw new TokenException("엑세스 토큰이 만료됐거나 올바르지 않습니다.");
    }

    final String token = JwtUtils.replaceTokenPrefix(header);
    final String userEmail = JwtUtils.getClaim(token, JwtProperties.USER_EMAIL).asString();

    if (checkBlacklistToken(token, userEmail)) {
      throw new TokenException("로그아웃 된 엑세스 토큰 입니다. 재로그인이 필요합니다.");
    }

    if (userEmail != null) {
      setAuthenticationContext(userEmail);
    }
    chain.doFilter(request, response);
  }

  private boolean checkBlacklistToken(
      @NotNull final String prefixToken, @NotNull final String userEmail) {
    final String blackPrefixToken =
        redisService.getValue(userEmail + JwtProperties.BLACKLIST_POSTFIX);
    return prefixToken.equals(blackPrefixToken);
  }

  private void setAuthenticationContext(@NotNull final String userEmail) {
    final User foundByEmail = userService.findByEmail(userEmail);

    final PrincipalDetails principalDetails = new PrincipalDetails(foundByEmail);
    final Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            principalDetails, null, principalDetails.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
