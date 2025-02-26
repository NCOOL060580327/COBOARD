package com.example.demo.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode {

  // 200
  OK(HttpStatus.OK, "20001", "요청 성공"),
  UPDATED(HttpStatus.OK, "20002", "요청 성공 및 리소스 수정됨"),

  // 201
  CREATED(HttpStatus.CREATED, "20101", "요청 성공 및 리소스 생성됨"),

  // 202
  ACCEPTED(HttpStatus.ACCEPTED, "20201", "요청 수락됨, 처리 진행 중"),

  // 204
  DELETED(HttpStatus.NO_CONTENT, "20401", "요청 성공 및 리소스 삭제됨"),

  // 205
  RESET_CONTENT(HttpStatus.RESET_CONTENT, "20501", "요청 성공 및 콘텐츠 초기화됨"),

  // 206
  PARTIAL_CONTENT(HttpStatus.PARTIAL_CONTENT, "20601", "요청 성공 및 부분 콘텐츠 반환됨"),

  // 400 Bad Request
  NOT_VALID_PASSWORD(HttpStatus.BAD_REQUEST, "40001", "비밀번호는 영문, 숫자, 특수문자를 포함한 9~16글자여야 합니다."),
  MALFORMED_TOKEN(HttpStatus.BAD_REQUEST, "40002", "토큰의 형식이 올바르지 않습니다."),

  // 401 Unauthorized
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "40101", "유효하지 않은 토큰입니다."),
  AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "40102", "인증이 필요합니다."),
  TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "40103", "토큰의 유효기간이 지났습니다."),

  // 403 Forbidden
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "40301", "권한이 없습니다."),

  // 404 Not Found
  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "40401", "등록된 사용자가 없습니다."),

  // 409 Conflict
  DUPLICATE_EMAIL(HttpStatus.CONFLICT, "40901", "이미 등록된 이메일입니다.");

  private final HttpStatus httpStatus;
  private final String divideCode;
  private final String message;
}
