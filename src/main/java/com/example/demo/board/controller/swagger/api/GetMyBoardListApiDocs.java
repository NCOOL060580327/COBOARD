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

public class GetMyBoardListApiDocs {

  @Operation(summary = "내 게시판 목록 조회", description = "자신이 속한 게시판 목록을 조회합니다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "게시판 목록 조회 성공",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BaseResponse.class),
                    examples = @ExampleObject(value = BoardSwaggerConst.GET_MY_BOARD_LIST_SUCCESS)))
      })
  public void getMyBoardList() {}
}
