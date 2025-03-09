package com.example.demo.quiz.facade;

import org.springframework.stereotype.Component;

import com.example.demo.quiz.dto.CreateProblemRequestDto;
import com.example.demo.quiz.service.ProblemCommandService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProblemFacade {

  private final ProblemCommandService problemCommandService;

  /**
   * 주어진 요청 데이터를 기반으로 새 문제를 생성합니다.
   *
   * @param requestDto 문제 생성 요청 데이터 {@link CreateProblemRequestDto} (문제 제목, 내용, 출처, 링크, 입출력 예시)
   */
  public void createProblem(CreateProblemRequestDto requestDto) {
    problemCommandService.createProblem(requestDto);
  }
}
