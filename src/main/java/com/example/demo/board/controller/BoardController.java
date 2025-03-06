package com.example.demo.board.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.board.controller.swagger.api.CreateBoardApiDocs;
import com.example.demo.board.dto.BoardCreateRequestDto;
import com.example.demo.board.dto.BoardCreateResponseDto;
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
}
