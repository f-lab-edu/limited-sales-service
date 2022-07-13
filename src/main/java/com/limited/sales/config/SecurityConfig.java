package com.limited.sales.config;

import com.limited.sales.filter.CustomLogoutHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final CustomAuthCheck customAuthCheck;
  private final CustomLogoutHandlerFilter customLogoutHandlerFilter;

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
                    .addLogoutHandler(customLogoutHandlerFilter)
                    .logoutSuccessHandler(
                        (request, response, authentication) -> {
                          response.setStatus(200);
                        }))
        .build();
  }
}
