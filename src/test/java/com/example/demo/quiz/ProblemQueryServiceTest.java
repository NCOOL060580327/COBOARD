package com.example.demo.quiz;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.ProblemException;
import com.example.demo.quiz.entity.Problem;
import com.example.demo.quiz.entity.repository.ProblemRepository;
import com.example.demo.quiz.service.ProblemQueryService;

@ExtendWith(MockitoExtension.class)
public class ProblemQueryServiceTest {

  @Mock private ProblemRepository problemRepository;

  @InjectMocks private ProblemQueryService problemQueryService;

  private final Long testId = Long.parseLong(ProblemTestConst.TEST_ID.getValue());
  private final String testTitle = ProblemTestConst.TEST_TITLE.getValue();
  private final String testDescription = ProblemTestConst.TEST_DESCRIPTION.getValue();

  @BeforeEach
  void setUp() {}

  @Nested
  @DisplayName("문제를 ID로 조회할 때")
  class getProblemById {
    @Test
    @DisplayName("문제를 반환합니다.")
    void getProblemById_Success() {
      // give
      Problem testProblem = Problem.builder().title(testTitle).description(testDescription).build();

      when(problemRepository.findById(testId)).thenReturn(Optional.of(testProblem));

      // when
      Problem problem = problemQueryService.getProblemById(testId);

      // then
      assertNotNull(problem, "반환된 문제가 null이면 안됩니다");
      assertEquals(testTitle, problem.getTitle(), "문제 제목이 일치해야 합니다");
      assertEquals(testDescription, problem.getDescription(), "문제 설명이 일치해야 합니다");
      verify(problemRepository, times(1)).findById(testId);
    }

    @Test
    @DisplayName("문제가 존재하지 않으면 예외를 발생시킵니다.")
    void getProblemById_Fail_Member_Not_Found() {
      // give
      when(problemRepository.findById(testId)).thenReturn(Optional.empty());

      // when
      ProblemException exception =
          assertThrows(
              ProblemException.class,
              () -> problemQueryService.getProblemById(testId),
              "문제가 존재하지 않으면 예외가 발생합니다.");

      // then
      assertEquals(GlobalErrorCode.PROBLEM_NOT_FOUND, exception.getErrorCode(), "예외 코드가 일치해야 합니다");
      verify(problemRepository, times(1)).findById(testId);
    }
  }
}
