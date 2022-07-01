package com.limited.sales.config.jwt;

import com.google.gson.Gson;
import com.limited.sales.config.auth.PrincipalDetails;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtTokenCreateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            final Gson gson = new Gson();
            final User user = gson.fromJson(new InputStreamReader(request.getInputStream()), User.class);
            log.info("{}", user);

            final UsernamePasswordAuthenticationToken createToken =
                    new UsernamePasswordAuthenticationToken(user.getUserEmail(), user.getUserPassword());

            final Authentication authentication =
                    authenticationManager.authenticate(createToken);

            final PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.info("{}", principalDetails.getUser().getUserEmail());

            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("로그인 실패");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        final PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        final JwtTokenCreateUtils utils = new JwtTokenCreateUtils();

        final String accessToken = utils.createAccessTokenMethod(principalDetails.getUser());
        final String refreshToken = utils.createRefreshTokenMethod(principalDetails.getUser());

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
    }
}
