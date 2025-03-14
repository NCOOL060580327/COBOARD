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
  PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "40003", "비밀번호가 일치하지 않습니다."),
  ALREADY_BLACKLIST_TOKEN(HttpStatus.BAD_REQUEST, "40004", "이미 블랙리스트에 존재합니다."),

  // 401 Unauthorized
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "40101", "유효하지 않은 토큰입니다."),
  AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "40102", "인증이 필요합니다."),
  TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "40103", "토큰의 유효기간이 지났습니다."),
  REFRESH_TOKEN_MISMATCH(HttpStatus.BAD_REQUEST, "40104", "토큰이 일치하지 않습니다."),

  // 403 Forbidden
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "40301", "권한이 없습니다."),

  // 404 Not Found
  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "40401", "등록된 사용자가 없습니다."),
  PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND, "40402", "등록된 문제가 없습니다."),
  BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "40403", "등록된 게시판이 없습니다."),
  BOARD_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "40404", "등록된 게시판 사용자가 없습니다."),
  POST_NOT_FOUND(HttpStatus.NOT_FOUND, "40405", "등록된 게시물이 없습니다."),
  POST_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "40406", "게시물 좋아요가 없습니다."),

  // 409 Conflict
  DUPLICATE_EMAIL(HttpStatus.CONFLICT, "40901", "이미 등록된 이메일입니다."),
  DUPLICATE_LIKE(HttpStatus.CONFLICT, "40902", "이미 좋아요가 존재합니다.");

  private final HttpStatus httpStatus;
  private final String divideCode;
  private final String message;
}
