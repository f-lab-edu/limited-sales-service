package com.limited.sales.jwt;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class JwtTokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 토큰 : limited
        // id, pw 정상적으로 들어와서 로그인이 완료되면, 토큰을 만들어주고 응답해준다.
        // 요청할때마다 header에 Authorization에 value 값으로 토큰을 가져올것이다.
        //그때 토큰이 넘어오면, 이 토큰이 내가 만든 토큰이 맞는지 검증한다.
        if(req.getMethod().equals("POST")) {
            String headerAuth = req.getHeader("Authorization");
            log.debug("=============== Authorization = {}", headerAuth);

            if(headerAuth.equals("limited")) {
                chain.doFilter(req, res);
            }else {
                PrintWriter out = res.getWriter();
                out.println("인증실패");
            }
        } else {
            chain.doFilter(req, res);
        }
    }
}
