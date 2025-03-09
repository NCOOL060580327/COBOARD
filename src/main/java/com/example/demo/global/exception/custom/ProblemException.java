package com.example.demo.global.exception.custom;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.GlobalException;

public class ProblemException extends GlobalException {
  public ProblemException(GlobalErrorCode errorCode) {
    super(errorCode);
  }
}
