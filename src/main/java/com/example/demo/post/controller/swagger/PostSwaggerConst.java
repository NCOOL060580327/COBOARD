package com.example.demo.post.controller.swagger;

public class PostSwaggerConst {

  public static final String CREATE_POST_SUCCESS =
      """
            {
              "isSuccess": true,
              "code": "201",
              "message": "요청 성공 및 리소스 생성됨",
              "divideCode": "20101",
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

  public static final String GET_POST_IN_BOARD_SUCCESS =
      """
                {
                  "isSuccess": true,
                  "code": "200",
                  "message": "요청 성공",
                  "divideCode": "20001",
                  "data": {
                    "content": [
                      {
                        "title": "string",
                        "likeCount": 0,
                        "averageRating": 0,
                        "createdAt": "",
                        "nickname": "string"
                      }
                    ],
                    "page": {
                      "size": 10,
                      "number": 0,
                      "totalElements": 0,
                      "totalPages": 0
                    }
                  }
                }
                """;

  public static final String CREATE_POST_LIKE_SUCCESS =
      """
                  {
                    "isSuccess": true,
                    "code": "200",
                    "message": "요청 성공",
                    "divideCode": "20001",
                    "data": {
                      "likeStatus": true,
                      "description": "좋아요가 추가되었습니다."
                    }
                  }
            """;

  public static final String CREATE_POST_LIKE_FAIL_DUPLICATE_EMAIL =
      """
                  {
                    "isSuccess": false,
                    "code": "409",
                    "message": "이미 좋아요가 존재합니다.",
                    "divideCode": "40902"
                  }
            """;

  public static final String DELETE_POST_LIKE_SUCCESS =
      """
                  {
                    "isSuccess": true,
                    "code": "200",
                    "message": "요청 성공",
                    "divideCode": "20001",
                    "data": {
                      "likeStatus": false,
                      "description": "좋아요가 삭제되었습니다."
                    }
                  }
            """;

  public static final String DELETE_POST_LIKE_FAIL_POST_LIKE_NOT_FOUND =
      """
                  {
                    "isSuccess": false,
                    "code": "404",
                    "message": "게시물 좋아요가 없습니다.",
                    "divideCode": "40406"
                  }
            """;
}
