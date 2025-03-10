package com.example.demo.post.controller.swagger;

public class PostSwaggerConst {

  public static final String CREATE_POST_SUCCESS =
      """
            {
              "isSuccess": true,
              "code": "201",
              "message": "요청 성공 및 리소스 생성됨",
              "divideCode": "20101",
              "timestamp": "2025-03-10 14:48:53"
            }
            """;

  public static final String BOARD_NOT_FOUND =
      """
              {
                "isSuccess": false,
                "code": "404",
                "message": "등록된 게시판이 없습니다.",
                "divideCode": "40403",
                "data": null
              }
            """;

  public static final String BOARD_MEMBER_NOT_FOUND =
      """
              {
                "isSuccess": false,
                "code": "404",
                "message": "등록된 게시판 회원이 없습니다.",
                "divideCode": "40404",
                "data": null
              }
            """;
}
