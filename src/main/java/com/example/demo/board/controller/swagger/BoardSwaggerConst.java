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

  public static final String GET_MY_BOARD_LIST_SUCCESS =
      """
          {
            "isSuccess": true,
            "code": "200",
            "message": "요청에 성공하였습니다.",
            "divideCode": "success",
            "data": [
              {
                "id": 1,
                "name": "string1",
                "thumbnailImage": "string"
              }
            ]
          }
          """;
}
