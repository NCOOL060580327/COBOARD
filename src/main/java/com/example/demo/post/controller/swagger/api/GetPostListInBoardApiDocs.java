package com.example.demo.post.controller.swagger.api;

import org.springframework.http.MediaType;

import com.example.demo.global.response.BaseResponse;
import com.example.demo.post.controller.swagger.PostSwaggerConst;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public class GetPostListInBoardApiDocs {
  @Operation(summary = "게시글 페이지 조회", description = "게시글을 페이지로 조회합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "20001",
            description = "게시글 조회 성공",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(value = PostSwaggerConst.GET_POST_IN_BOARD_SUCCESS)))
      })
  public void getPostListInBoard() {}
}
