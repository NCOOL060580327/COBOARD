package com.example.demo.global.exception.custom;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.GlobalException;

public class AuthException extends GlobalException {
  public AuthException(GlobalErrorCode errorCode) {
    super(errorCode);
  }
}
