package com.example.demo.member.controller.swagger.api;

import org.springframework.http.MediaType;

import com.example.demo.global.response.BaseResponse;
import com.example.demo.member.controller.swagger.SwaggerConst;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public class SignUpApiDocs {

  @Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "회원가입 성공",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(value = SwaggerConst.SIGNUP_SUCCESS))),
        @ApiResponse(
            responseCode = "400",
            description = "유효하지 않은 비밀번호",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(value = SwaggerConst.PASSWORD_MISMATCH))),
        @ApiResponse(
            responseCode = "409",
            description = "이미 존재하는 이메일",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(value = SwaggerConst.DUPLICATE_EMAIL)))
      })
  public void signUpMember() {}
}
