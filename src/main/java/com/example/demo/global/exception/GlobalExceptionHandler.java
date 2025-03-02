package com.example.demo.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.global.response.BaseResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = {GlobalException.class})
  protected ResponseEntity<BaseResponse<Void>> handleCustomException(GlobalException e) {
    log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(BaseResponse.onFailure(e.getErrorCode(), null));
  }
}
