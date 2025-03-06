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

public class GetMemberByIdentifiedCodeApiDocs {

  @Operation(summary = "게시판 생성", description = "게시판을 생성하고 회원을 초대합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "요청 성공",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples =
                        @ExampleObject(
                            value = MemberSwaggerConst.GET_MEMBER_BY_IDENTIFIED_CODE_SUCCESS))),
        @ApiResponse(
            responseCode = "404",
            description = "회원이 존재하지 않음",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(value = MemberSwaggerConst.MEMBER_NOT_FOUND)))
      })
  public void getMemberByIdentifiedCode() {}
}
