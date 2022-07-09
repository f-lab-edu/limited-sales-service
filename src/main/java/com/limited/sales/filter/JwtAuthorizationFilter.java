package com.limited.sales.filter;

import com.limited.sales.principal.PrincipalDetails;
import com.limited.sales.utils.JwtProperties;
import com.limited.sales.exception.sub.TokenException;
import com.limited.sales.user.UserService;
import com.limited.sales.user.vo.User;
import com.limited.sales.utils.JwtProvider;
import com.limited.sales.utils.JwtValidation;
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

@Slf4j
public final class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final UserService userService;
    private final JwtProvider jwtProvider = new JwtProvider();
    private final JwtValidation jwtValidation = new JwtValidation();


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String header = request.getHeader(JwtProperties.HEADER_STRING);

        if(jwtValidation.isValidationAuthorizationCheck(header))    {chain.doFilter(request, response); return;}
        if(jwtValidation.isValidationAccessTokenCheck(header))      throw new TokenException("Access 토큰이 비정상적 입니다."); //억세스 토큰이 만료될 때 익셉션 처리 할 것.

        final String prefixToken = jwtProvider.replaceTokenPrefix(header);
        final String userEmail = jwtProvider.getClaim(prefixToken, "userEmail").asString();

        if(userEmail != null)                                       userAuthority(userEmail);
        chain.doFilter(request, response);
    }

    private void userAuthority(String userEmail) {
        final User byUser = userService.findByUser(userEmail);

        final PrincipalDetails principalDetails = new PrincipalDetails(byUser);
        final Authentication authentication = new UsernamePasswordAuthenticationToken(
                principalDetails,
                null,
                principalDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
