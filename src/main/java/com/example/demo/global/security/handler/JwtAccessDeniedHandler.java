package com.example.demo.global.security.handler;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.response.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  private static final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException {
    log.warn(
        "요청에 대한 접근 거부: {}, 원인: {}", request.getRequestURI(), accessDeniedException.getMessage());
    sendErrorResponse(response, accessDeniedException);
  }

  private void sendErrorResponse(HttpServletResponse response, AccessDeniedException exception)
      throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.FORBIDDEN.value());
    BaseResponse<Object> errorResponse =
        BaseResponse.onFailure(GlobalErrorCode.ACCESS_DENIED, exception.getMessage());
    mapper.writeValue(response.getOutputStream(), errorResponse);
  }
}
