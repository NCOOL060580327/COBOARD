package com.example.demo.quiz.controller.swagger.api;

import org.springframework.http.MediaType;

import com.example.demo.global.response.BaseResponse;
import com.example.demo.quiz.controller.swagger.ProblemSwaggerConst;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public class CreateProblemApiDocs {

  @Operation(summary = "문제 생성", description = "코딩 테스트 문제를 생성합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "문제 생성 성공",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(value = ProblemSwaggerConst.PROBLEM_CREATE_SUCCESS)))
      })
  public void createProblem() {}
}
