package com.example.demo.board.controller.swagger.api;

import org.springframework.http.MediaType;

import com.example.demo.board.controller.swagger.BoardSwaggerConst;
import com.example.demo.global.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public class CreateBoardApiDocs {

  @Operation(summary = "게시판 생성", description = "게시판을 생성하고 회원을 초대합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "게시판 생성 성공",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(value = BoardSwaggerConst.BOARD_CREATE_SUCCESS)))
      })
  public void createBoard() {}
}
