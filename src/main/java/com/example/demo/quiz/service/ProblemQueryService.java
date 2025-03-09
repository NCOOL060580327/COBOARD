package com.example.demo.quiz.service;

import org.springframework.stereotype.Service;

import com.example.demo.global.annotation.ReadOnlyTransactional;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.ProblemException;
import com.example.demo.quiz.entity.Problem;
import com.example.demo.quiz.entity.repository.ProblemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ReadOnlyTransactional
public class ProblemQueryService {

  private final ProblemRepository problemRepository;

  /**
   * 주어진 문제 ID를 통해 문제를 조회합니다.
   *
   * @param problemId 조회할 문제의 ID
   * @return 조회된 {@link Problem} 엔티티
   * @throws ProblemException {@code GlobalErrorCode.PROBLEM_NOT_FOUND} - 해당 ID로 문제를 찾을 수 없는 경우
   */
  public Problem getProblemById(Long problemId) {
    return problemRepository
        .findById(problemId)
        .orElseThrow(() -> new ProblemException(GlobalErrorCode.PROBLEM_NOT_FOUND));
  }
}
