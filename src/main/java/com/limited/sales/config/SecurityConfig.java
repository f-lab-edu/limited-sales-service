package com.limited.sales.config;

import com.limited.sales.config.jwt.JwtAuthenticationFilter;
import com.limited.sales.config.jwt.JwtAuthorizationFilter;
import com.limited.sales.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf()
                .disable()
                .apply(new CustomAuthCheck())
                .and()
                .authorizeRequests(authorize ->
                        authorize.antMatchers(HttpMethod.POST, "/user").permitAll()
                                .antMatchers(HttpMethod.GET, "/user/email").permitAll()
                                .antMatchers("/user/**").access("hasRole('ROLE_USER')")
                )
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .build();
    }

    public class CustomAuthCheck extends AbstractHttpConfigurer<CustomAuthCheck, HttpSecurity>{
        @Override
        public void configure(HttpSecurity builder) {
            final AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userService))
            ;
        }
    }
}
