package com.example.demo.global.security.handler;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.response.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException) {

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());

    BaseResponse<Object> errorResponse =
        BaseResponse.onFailure(GlobalErrorCode.AUTHENTICATION_REQUIRED, authException.getMessage());

    try {
      objectMapper.writeValue(response.getOutputStream(), errorResponse);
    } catch (IOException e) {
      log.error("오류 응답을 작성하지 못했습니다", e);
      try {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.getOutputStream().print("{\"error\": \"Internal server error\"}");
      } catch (IOException ioe) {
        log.error("fallback 응답을 작성하지 못했습니다", ioe);
      }
    }
  }
}
