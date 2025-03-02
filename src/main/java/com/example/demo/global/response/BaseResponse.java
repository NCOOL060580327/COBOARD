package com.example.demo.global.response;

import java.time.LocalDateTime;

import com.example.demo.global.exception.GlobalErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class BaseResponse<T> {

  @JsonProperty("isSuccess")
  private Boolean isSuccess;

  private String code;
  private String divideCode;
  private String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T data;

  @JsonProperty("timestamp")
  private final LocalDateTime timestamp;

  // 성공한 경우 응답 생성
  public static <T> BaseResponse<T> onSuccess(T data) {
    return new BaseResponse<>(true, "200", "success", "요청에 성공하였습니다.", data, LocalDateTime.now());
  }

  public static <T> BaseResponse<T> onSuccess(GlobalErrorCode code, T data) {
    return new BaseResponse<>(
        true,
        String.valueOf(code.getHttpStatus().value()),
        code.getDivideCode(),
        code.getMessage(),
        data,
        LocalDateTime.now());
  }

  // 실패한 경우 응답 생성
  public static <T> BaseResponse<T> onFailure(GlobalErrorCode code, T data) {
    return new BaseResponse<>(
        false,
        String.valueOf(code.getHttpStatus().value()),
        code.getDivideCode(),
        code.getMessage(),
        data,
        LocalDateTime.now());
  }
}
