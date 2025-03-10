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

public class CreatePostApiDocs {

  @Operation(summary = "게시글 생성", description = "게시글을 생성합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "게시글 생성 성공",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(value = PostSwaggerConst.CREATE_POST_SUCCESS))),
        @ApiResponse(
            responseCode = "40403",
            description = "게시판이 존재하지 않음",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(value = PostSwaggerConst.BOARD_NOT_FOUND))),
        @ApiResponse(
            responseCode = "40404",
            description = "게시판 회원이 존재하지 않음",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(value = PostSwaggerConst.BOARD_MEMBER_NOT_FOUND)))
      })
  public void createPost() {}
}
