package com.example.demo.quiz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.global.annotation.SwaggerDocs;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.response.BaseResponse;
import com.example.demo.quiz.controller.swagger.api.CreateDailyProblemApiDocs;
import com.example.demo.quiz.controller.swagger.api.CreateProblemApiDocs;
import com.example.demo.quiz.dto.CreateProblemRequestDto;
import com.example.demo.quiz.facade.ProblemFacade;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problem")
public class ProblemController {

  private final ProblemFacade problemFacade;

  @PostMapping("/")
  @SwaggerDocs(CreateProblemApiDocs.class)
  public ResponseEntity<BaseResponse<Void>> createProblem(
      @RequestBody CreateProblemRequestDto requestDto) {
    problemFacade.createProblem(requestDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.onSuccess(GlobalErrorCode.CREATED, null));
  }

  @PostMapping("/daily")
  @SwaggerDocs(CreateDailyProblemApiDocs.class)
  public ResponseEntity<BaseResponse<Void>> createDailyProblem(
      @RequestParam Long boardId, @RequestParam Long problemId) {
    problemFacade.createDailyProblem(boardId, problemId);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.onSuccess(GlobalErrorCode.CREATED, null));
  }
}
