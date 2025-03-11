package com.example.demo.global.exception.custom;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.GlobalException;

public class PostException extends GlobalException {
  public PostException(GlobalErrorCode errorCode) {
    super(errorCode);
  }
}
