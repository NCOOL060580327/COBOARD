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

public class DeletePostLikeApiDocs {

  @Operation(summary = "게시글 좋아요 삭제", description = "게시글 좋아요를 삭제합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "게시글 좋아요 삭제 성공",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(value = PostSwaggerConst.DELETE_POST_LIKE_SUCCESS))),
        @ApiResponse(
            responseCode = "40406",
            description = "게시판이 존재하지 않음",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples =
                        @ExampleObject(
                            value = PostSwaggerConst.DELETE_POST_LIKE_FAIL_POST_LIKE_NOT_FOUND)))
      })
  public void deletePostLike() {}
}
