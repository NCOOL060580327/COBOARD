package com.example.demo.quiz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.global.annotation.SwaggerDocs;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.response.BaseResponse;
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
}
