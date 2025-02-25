package com.example.demo.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode {
  ;

  private final HttpStatus httpStatus;
  private final String divideCode;
  private final String message;
}
