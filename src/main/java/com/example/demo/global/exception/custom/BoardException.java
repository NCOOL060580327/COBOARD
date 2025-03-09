package com.example.demo.global.exception.custom;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.GlobalException;

public class BoardException extends GlobalException {
  public BoardException(GlobalErrorCode errorCode) {
    super(errorCode);
  }
}
