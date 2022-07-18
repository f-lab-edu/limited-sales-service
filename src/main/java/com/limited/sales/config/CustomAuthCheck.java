package com.limited.sales.config;

import com.limited.sales.filter.JwtAuthenticationFilter;
import com.limited.sales.filter.JwtAuthorizationFilter;
import com.limited.sales.redis.RedisService;
import com.limited.sales.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthCheck extends AbstractHttpConfigurer<CustomAuthCheck, HttpSecurity> {
  private final UserService userService;
  private final RedisService redisService;

  @Override
  public void configure(HttpSecurity builder) {
    final AuthenticationManager authenticationManager =
        builder.getSharedObject(AuthenticationManager.class);
    builder
        .addFilter(new JwtAuthenticationFilter(authenticationManager, redisService))
        .addFilter(new JwtAuthorizationFilter(authenticationManager, userService, redisService));
  }
}
