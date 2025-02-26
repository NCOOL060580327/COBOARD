package com.example.demo.global.exception.custom;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.GlobalException;

public class TokenException extends GlobalException {
  public TokenException(GlobalErrorCode errorCode) {
    super(errorCode);
  }
}
