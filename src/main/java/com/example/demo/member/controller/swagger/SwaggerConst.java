package com.example.demo.member.controller.swagger;

public class SwaggerConst {

  public static final String CREATE_SUCCESS =
      """
        {
          "isSuccess": true,
          "code": "201",
          "message": "요청 성공 및 리소스 생성됨",
          "divideCode": "20101",
          "data": null
        }
        """;

  public static final String NOT_VALID_PASSWORD =
      """
        {
          "isSuccess": false,
          "code": "400",
          "message": "비밀번호는 영문, 숫자, 특수문자를 포함한 9~16글자여야 합니다.",
          "divideCode": "40001"
        }
        """;

  public static final String DUPLICATE_EMAIL =
      """
        {
          "isSuccess": false,
          "code": "409",
          "message": "이미 존재하는 이메일입니다.",
          "divideCode": "40901"
        }
        """;
}
