package com.limited.sales.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limited.sales.auth.PrincipalDetails;
import com.limited.sales.redis.RedisService;
import com.limited.sales.user.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final RedisService redisService;

    private final JwtProvider provider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationManager authenticationManager1, RedisService redisService, JwtProvider provider) {
        this.authenticationManager = authenticationManager1;
        this.provider = provider;
        super.setAuthenticationManager(authenticationManager);
        this.redisService = redisService;
    }



    // Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
    // 인증 요청시에 실행되는 함수 => /login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class); //user에 담기게 된다.
            log.debug("============ user : {}", user);

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(user.getUserEmail(), user.getUserPassword());

            log.debug("============== token : {}" , authenticationToken);

            // PrincipalDetailsService.loadByUsername() 함수가 실행된 후
            // 정상이면 authentication이 리턴됨.
            // db랑 아이디 비번 정보가 일치한다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.debug("============== 로그인 완료됨 : {}" , principalDetails.getUser().getUserEmail()); //로그인 정상적으로 되었다는 뜻.

            return authentication;

        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 인증 완료 후, 토큰 발급 받는 곳
     * @param request
     * @param response
     * @param chain
     * @param authResult the object returned from the <tt>attemptAuthentication</tt>
     * method.
     * @throws IOException
     * @throws ServletException
     */
    //attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행된다.
    //jwt 토큰을 만들어서 request 요청한 사용자에게 jwt token을 response해주면 된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        System.out.println("successfulAuthentication 실행됨 : 인증완료되었다는 뜻임.");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        JwtProvider jwtProvider = new JwtProvider();

        //토큰을 새로 만든다.
        String accessToken = jwtProvider.getToken(principalDetails, "accessToken");
        String refreshToken = jwtProvider.getToken(principalDetails, "refreshToken");

        log.debug("=================== refreshToken 발급 : {}" , refreshToken);

        //refreshToken은 vo에 저장해준다.
        principalDetails.getUser().setRefreshToken(refreshToken);
        redisService.setValue(principalDetails.getUser().getUserEmail(), refreshToken, Duration.ofMillis(JwtProperties.REFRESH_EXPIRATION_TIME));
        log.debug("=================== vo에 저장된 refreshToken  : {}" , principalDetails.getUser().getRefreshToken());
        log.debug("=================== Redis에 저장된 refreshToken  : {}" , redisService.getValues("daramii@naver.com"));

        //accessToken을 발급해준다. response에 Authorization으로 실어준다.
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken); //한칸 반드시 띄워줘야함.

    }
}
