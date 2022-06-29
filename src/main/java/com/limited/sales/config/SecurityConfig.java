package com.limited.sales.config;

import com.limited.sales.jwt.JwtTokenFilter;
import com.limited.sales.jwt.JwtAuthenticationFilter;
import com.limited.sales.jwt.JwtAuthorizationFilter;
import com.limited.sales.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CorsConfig corsConfig;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .formLogin()
                .usernameParameter("userEmail")
                .passwordParameter("userPassword")
                .disable()
            .httpBasic()
                .disable()
            .apply(new MyCustomDsl())
            .and()
            .authorizeRequests(authorize -> authorize.antMatchers("/user/**") //이쪽으로 주소가 들어오면
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')") //이 경우만 접근가능하다.
                    //.antMatchers("/api/v1/manager/**")
                    //.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                    //.antMatchers("/api/v1/admin/**")
                    //.access("hasRole('ROLE_ADMIN')")
                    .anyRequest()
                    .permitAll());
        return http.build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {

        @Override
        public void configure(HttpSecurity http) throws Exception {

            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsConfig.corsFilter())  //모든 요청에 이 필터를 탄다. @CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O)
                    .addFilter(new JwtAuthenticationFilter(authenticationManager))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userMapper));
        }
    }

}
