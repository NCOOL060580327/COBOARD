package com.example.demo.global.exception.custom;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.GlobalException;

public class MemberException extends GlobalException {
  public MemberException(GlobalErrorCode errorCode) {
    super(errorCode);
  }
}
