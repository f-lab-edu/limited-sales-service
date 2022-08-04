package com.limited.sales.config;

import com.limited.sales.filter.JwtLogoutHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final CustomAuthCheck customAuthCheck;
  private final JwtLogoutHandlerFilter jwtLogoutHandlerFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .csrf()
        .disable()
        .apply(customAuthCheck)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .httpBasic()
        .disable()
        .formLogin()
        .disable()
        .logout(
            logout ->
                logout
                    .logoutUrl("/logout")
                    .addLogoutHandler(jwtLogoutHandlerFilter)
                    .logoutSuccessHandler(
                        (request, response, authentication) -> {
                          response.setStatus(200);
                        }))
        .build();
  }
}
