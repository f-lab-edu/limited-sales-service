package com.limited.sales.filter;

import com.limited.sales.config.GsonSingleton;
import com.limited.sales.exception.ExceptionVo;
import com.limited.sales.exception.sub.BadRequestException;
import com.limited.sales.exception.sub.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      filterChain.doFilter(request, response);
    } catch (BadRequestException | TokenException | IllegalArgumentException e) {
      log.debug(":: ExceptionHandlerFilter.doFilterInternal.BAD_REQUEST ::");

      log.error(e.toString());
      setExceptionResponse(HttpStatus.BAD_REQUEST, response, e);
    } catch (RuntimeException e) {
      log.debug(":: ExceptionHandlerFilter.doFilterInternal.INTERNAL_SERVER_ERROR ::");

      e.printStackTrace();
      setExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, e);
    }
  }

  public void setExceptionResponse(HttpStatus status, HttpServletResponse response, Throwable ex) {
    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    ExceptionVo<Void> exceptionVo =
        ExceptionVo.<Void>builder().status(status).message(ex.getMessage()).build();
    try {
      log.debug(":: ExceptionHandlerFilter.setExceptionResponse = {} ::", exceptionVo);
      response.getWriter().write(GsonSingleton.getGson().toJson(exceptionVo));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
