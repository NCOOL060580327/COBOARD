package com.example.demo.board.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.demo.board.controller.swagger.api.CreateBoardApiDocs;
import com.example.demo.board.controller.swagger.api.GetMyBoardListApiDocs;
import com.example.demo.board.dto.request.BoardCreateRequestDto;
import com.example.demo.board.dto.response.BoardCreateResponseDto;
import com.example.demo.board.dto.response.GetMyBoardListResponseDto;
import com.example.demo.board.facade.BoardFacade;
import com.example.demo.global.annotation.SwaggerDocs;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.response.BaseResponse;
import com.example.demo.global.security.domain.MemberDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

  private final BoardFacade boardFacade;

  @PostMapping("/")
  @SwaggerDocs(CreateBoardApiDocs.class)
  public ResponseEntity<BaseResponse<BoardCreateResponseDto>> createBoard(
      @RequestBody BoardCreateRequestDto requestDto,
      @AuthenticationPrincipal MemberDetails memberDetails) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            BaseResponse.onSuccess(
                GlobalErrorCode.CREATED,
                BoardCreateResponseDto.fromBoard(
                    boardFacade.createBoard(requestDto, memberDetails.getMember()))));
  }

  @GetMapping("/my-board")
  @SwaggerDocs(GetMyBoardListApiDocs.class)
  public ResponseEntity<BaseResponse<List<GetMyBoardListResponseDto>>> getMyBoardList(
      @AuthenticationPrincipal MemberDetails memberDetails,
      @RequestParam(required = false) Long lastBoardId) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            BaseResponse.onSuccess(
                boardFacade.getMyBoardList(memberDetails.getMember(), lastBoardId)));
  }
}
