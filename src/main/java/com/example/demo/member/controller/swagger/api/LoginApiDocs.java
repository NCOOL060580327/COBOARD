package com.example.demo.member.controller.swagger.api;

import org.springframework.http.MediaType;

import com.example.demo.global.response.BaseResponse;
import com.example.demo.member.controller.swagger.MemberSwaggerConst;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public class LoginApiDocs {

  @Operation(summary = "로그인", description = "로그인을 합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(value = MemberSwaggerConst.LOGIN_SUCCESS))),
        @ApiResponse(
            responseCode = "400",
            description = "비밀번호 불일치",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(value = MemberSwaggerConst.PASSWORD_MISMATCH))),
        @ApiResponse(
            responseCode = "404",
            description = "회원이 존재하지 않음",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(value = MemberSwaggerConst.MEMBER_NOT_FOUND)))
      })
  public void login() {}
}
