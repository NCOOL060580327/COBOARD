package com.example.demo.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode {

  // 400 Bad Request
  NOT_VALID_PASSWORD(
      HttpStatus.BAD_REQUEST, "bad-request-001", "비밀번호는 영문, 숫자, 특수문자를 포함한 9~16글자여야 합니다."),
  MALFORMED_TOKEN(HttpStatus.BAD_REQUEST, "bad-request-001", "토큰의 형식이 올바르지 않습니다."),

  // 401 Unauthorized
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "unauthorized-001", "유효하지 않은 토큰입니다."),
  AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "unauthorized-002", "인증이 필요합니다."),
  TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "unauthorized-003", "토큰의 유효기간이 지났습니다."),

  // 403 Forbidden - 권한 없음
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "forbidden-001", "권한이 없습니다."),

  // 404 Not Found
  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "not-found-001", "등록된 사용자가 없습니다.");

  private final HttpStatus httpStatus;
  private final String divideCode;
  private final String message;
}
