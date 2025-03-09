package com.example.demo.quiz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.board.entity.Board;
import com.example.demo.quiz.entity.DailyProblem;
import com.example.demo.quiz.entity.Problem;
import com.example.demo.quiz.entity.repository.DailyProblemRepository;
import com.example.demo.quiz.service.DailyProblemCommandService;

@ExtendWith(MockitoExtension.class)
public class DailyProblemCommandServiceTest {

  @Mock private DailyProblemRepository dailyProblemRepository;

  @InjectMocks private DailyProblemCommandService dailyProblemCommandService;

  @BeforeEach
  void setUp() {}

  private final Long testProblemId = Long.parseLong(ProblemTestConst.TEST_ID.getValue());
  private final String testTitle = ProblemTestConst.TEST_TITLE.getValue();
  private final String testDescription = ProblemTestConst.TEST_DESCRIPTION.getValue();
  private final Long testBoardId = Long.parseLong(ProblemTestConst.TEST_ID.getValue());
  private final String testBoardName = ProblemTestConst.TEST_TITLE.getValue();

  @Nested
  @DisplayName("일일 문제를 생성할 떄")
  class createDailyProblem {
    @Test
    @DisplayName("문제와 게시판을 통해 일일 문제를 생성합니다.")
    void createDailyProblem_Success() {
      // give
      Problem testProblem = Problem.builder().build();

      Board testBoard = Board.builder().build();

      LocalDate now = LocalDate.now();

      // when
      dailyProblemCommandService.createDailyProblem(testProblem, testBoard);

      // then
      ArgumentCaptor<DailyProblem> captor = ArgumentCaptor.forClass(DailyProblem.class);
      verify(dailyProblemRepository, times(1)).save(captor.capture());

      DailyProblem dailyProblem = captor.getValue();
      assertNotNull(dailyProblem, "저장된 일일 문제가 null이면 안됩니다");
      assertEquals(testProblem, dailyProblem.getProblem(), "문제가 일치해야 합니다");
      assertEquals(testBoard, dailyProblem.getBoard(), "게시판이 일치해야 합니다");
      assertEquals(now, dailyProblem.getCreatedAt(), "생성 날짜가 오늘 날짜와 일치해야 합니다");
    }
  }
}
