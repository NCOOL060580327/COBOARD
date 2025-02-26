package com.example.demo.global.security.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.global.exception.custom.AuthException;
import com.example.demo.global.response.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthExceptionHandlingFilter extends OncePerRequestFilter {

  private static final ObjectMapper mapper = new ObjectMapper();

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (AuthException e) {
      handleAuthException(response, e);
    }
  }

  private void handleAuthException(HttpServletResponse response, AuthException e)
      throws IOException {
    log.error("인증 실패: {}", e.getMessage(), e);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    BaseResponse<Object> errorResponse = BaseResponse.onFailure(e.getErrorCode(), e.getMessage());
    mapper.writeValue(response.getOutputStream(), errorResponse);
  }
}
