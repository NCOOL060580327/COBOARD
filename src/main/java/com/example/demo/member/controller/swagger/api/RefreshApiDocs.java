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

public class RefreshApiDocs {

  @Operation(summary = "토큰 재발급", description = "토큰을 재발급합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "토큰 갱신 성공",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(value = SwaggerConst.REFRESH_SUCCESS))),
        @ApiResponse(
            responseCode = "401",
            description = "유효하지 않은 토큰",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(value = SwaggerConst.INVALID_TOKEN)))
      })
  public void refresh() {}
}
