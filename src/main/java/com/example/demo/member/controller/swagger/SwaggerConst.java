package com.example.demo.member.controller.swagger;

public class SwaggerConst {

  public static final String SIGNUP_SUCCESS =
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

  public static final String LOGIN_SUCCESS =
      """
        {
          "isSuccess": true,
          "code": "200",
          "message": "요청 성공",
          "divideCode": "20001",
          "data": {
            "memberId": 1,
            "nickname": "exampleUser",
            "profileImage": "profile.png",
            "accessToken": "access-token-value",
            "refreshToken": "refresh-token-value"
          }
        }
        """;

  public static final String PASSWORD_MISMATCH =
      """
            {
              "isSuccess": false,
              "code": "400",
              "message": "비밀번호가 일치하지 않습니다.",
              "divideCode": "40003"
            }
            """;

  public static final String MEMBER_NOT_FOUND =
      """
        {
          "isSuccess": false,
          "code": "404",
          "message": "등록된 사용자가 없습니다.",
          "divideCode": "40401",
          "data": null
        }
        """;
}
