package com.example.demo.quiz.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.quiz.dto.CreateProblemRequestDto;
import com.example.demo.quiz.entity.Problem;
import com.example.demo.quiz.entity.repository.ProblemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProblemCommandService {

  private final ProblemRepository problemRepository;

  /**
   * 문제 생성 요청 데이터를 기반으로 새로운 문제를 생성합니다.
   *
   * @param requestDto 문제 생성 요청 데이터 {@link CreateProblemRequestDto} (문제 제목, 내용, 출처, 링크, 입출력 예시)
   */
  public void createProblem(CreateProblemRequestDto requestDto) {
    Problem problem =
        Problem.builder()
            .title(requestDto.title())
            .description(requestDto.description())
            .source(requestDto.source())
            .problemLink(requestDto.problemLink())
            .examples(requestDto.ioExampleList())
            .build();

    problemRepository.save(problem);
  }
}
