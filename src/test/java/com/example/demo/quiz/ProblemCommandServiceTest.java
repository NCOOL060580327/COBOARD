package com.example.demo.quiz;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.quiz.dto.CreateProblemRequestDto;
import com.example.demo.quiz.entity.IOExample;
import com.example.demo.quiz.entity.Problem;
import com.example.demo.quiz.entity.repository.ProblemRepository;
import com.example.demo.quiz.service.ProblemCommandService;

@ExtendWith(MockitoExtension.class)
public class ProblemCommandServiceTest {

  @Mock private ProblemRepository problemRepository;

  @InjectMocks private ProblemCommandService problemCommandService;

  private final String testTitle = ProblemTestConst.TEST_TITLE.getValue();
  private final String testDescription = ProblemTestConst.TEST_DESCRIPTION.getValue();
  private final String testSource = ProblemTestConst.TEST_SOURCE.getValue();
  private final String testProblemLink = ProblemTestConst.TEST_PROBLEM_LINK.getValue();
  private final List<IOExample> testExamples =
      List.of(
          IOExample.builder().input("input1").output("output1").build(),
          IOExample.builder().input("input2").output("output2").build());

  @BeforeEach
  void setUp() {}

  @Nested
  @DisplayName("문제를 생성할 때")
  class CreateProblem {
    @Test
    @DisplayName("CreateProblemRequestDto를 통해 문제를 생성합니다.")
    void createProblem_Success() {
      // give
      CreateProblemRequestDto requestDto =
          CreateProblemRequestDto.builder()
              .title(testTitle)
              .description(testDescription)
              .source(testSource)
              .problemLink(testProblemLink)
              .ioExampleList(testExamples)
              .build();

      Problem testProblem =
          Problem.builder()
              .title(testTitle)
              .description(testDescription)
              .source(testSource)
              .problemLink(testProblemLink)
              .examples(testExamples)
              .build();

      when(problemRepository.save(any(Problem.class))).thenReturn(testProblem);

      // when
      problemCommandService.createProblem(requestDto);

      // then
      ArgumentCaptor<Problem> captor = ArgumentCaptor.forClass(Problem.class);
      verify(problemRepository, times(1)).save(captor.capture());

      Problem savedProblem = captor.getValue();
      assertEquals(testTitle, savedProblem.getTitle(), "문제 제목이 일치해야 합니다");
      assertEquals(testDescription, savedProblem.getDescription(), "문제 설명이 일치해야 합니다");
      assertEquals(testSource, savedProblem.getSource(), "문제 출처가 일치해야합니다");
      assertEquals(testProblemLink, savedProblem.getProblemLink(), "문제 링크가 일치해야합니다");
      assertEquals(testExamples, savedProblem.getExamples(), "문제 입출력 예시가 일치해야합니다");
    }
  }
}
