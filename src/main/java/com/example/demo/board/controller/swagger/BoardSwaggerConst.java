package com.example.demo.board.controller.swagger;

public class BoardSwaggerConst {

  public static final String BOARD_CREATE_SUCCESS =
      """
              {
                "isSuccess": true,
                "code": "201",
                "message": "요청 성공 및 리소스 생성됨",
                "divideCode": "20101",
                "data": {
                  "boardId": 1
                }
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
