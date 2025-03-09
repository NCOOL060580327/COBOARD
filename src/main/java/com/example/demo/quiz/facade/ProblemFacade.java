package com.example.demo.quiz.facade;

import org.springframework.stereotype.Component;

import com.example.demo.board.entity.Board;
import com.example.demo.board.service.query.BoardQueryService;
import com.example.demo.quiz.dto.CreateProblemRequestDto;
import com.example.demo.quiz.entity.Problem;
import com.example.demo.quiz.service.DailyProblemCommandService;
import com.example.demo.quiz.service.ProblemCommandService;
import com.example.demo.quiz.service.ProblemQueryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProblemFacade {

  private final ProblemCommandService problemCommandService;
  private final ProblemQueryService problemQueryService;
  private final DailyProblemCommandService dailyProblemCommandService;

  private final BoardQueryService boardQueryService;

  /**
   * 주어진 요청 데이터를 기반으로 새 문제를 생성합니다.
   *
   * @param requestDto 문제 생성 요청 데이터 {@link CreateProblemRequestDto} (문제 제목, 내용, 출처, 링크, 입출력 예시)
   */
  public void createProblem(CreateProblemRequestDto requestDto) {
    problemCommandService.createProblem(requestDto);
  }

  /**
   * BoardId{@link Board}와 ProblemId{@link Problem}을 통해 게시판에 문제를 할당합니다.
   *
   * @param boardId 게시판 아이디
   * @param problemId 문제 아이디
   */
  public void createDailyProblem(Long boardId, Long problemId) {

    Board board = boardQueryService.getBoardById(boardId);

    Problem problem = problemQueryService.getProblemById(problemId);

    dailyProblemCommandService.createDailyProblem(problem, board);
  }
}
