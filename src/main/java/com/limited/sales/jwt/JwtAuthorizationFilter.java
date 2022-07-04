package com.limited.sales.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.limited.sales.auth.PrincipalDetails;
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

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserMapper userMapper;

    private RedisService redisService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserMapper userMapper, RedisService redisService) {
        super(authenticationManager);
        this.userMapper = userMapper;
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        System.out.println("인증이나 권한이 필요한 주소 요청이 됨");

        String jwtHeader= request.getHeader(JwtProperties.HEADER_STRING);
        System.out.println("jwtHeader : " + jwtHeader);

        // header가 있는지 확인.
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }

        // JWT access 토큰을 검증해서 정상적인 사용자인지 확인을 한다.
        String jwtToken = jwtHeader.replace(JwtProperties.TOKEN_PREFIX, "");

        //만료된 토큰인지 먼저 확인을 한다.
        String invalidToken = redisService.getValues(JwtProperties.BLACKLIST_TOKEN_PREFIX + jwtToken);

        log.debug("=========================== invalidToken : {}" , invalidToken);

        if(invalidToken == null) {

            // 서명이 정상적으로 되면, 유저네임을 가져온다.
            String userEmail = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("userEmail").asString();

            //이 유저네임을 통해서 레디스에 일치하는 refreshToken이 존재하는지 유효성을 검증한다.
            String redisRT = redisService.getValues(userEmail);

            log.debug("========================== {}의 refreshToken 검증  : {}", userEmail, redisRT);

            //서명이 정상적으로 됨. (= refresh 토큰 정보가 유효하다면 ) (= 만약 refresh 토큰이 만료된 상태라면, 미리 해둔 expired time 설정을 통해 redis 저장소에서 토큰은 자동으로 삭제된다.)
            if (redisRT != null) {
                User userEntity = userMapper.findByUserEmail(userEmail);

                PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
                //로그인을 하지 않더라도, 강제로 객체를 만들수있다.
                //지금은 로그인을 따로 한게 아니라, 토큰을 통해 서명이 잘 검증되어서, 유저네임이 있으면 authentication 객체를 만드는것이다.
                Authentication authentication
                        = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                //시큐리티를 저장할수있는 세션공간을 찾아서, (강제로 시큐리티의 세션에 접근하여) authentication 객체를 저장.
                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request, response);
            }
        }
    }
}
