package com.example.demo.quiz.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.board.entity.Board;
import com.example.demo.quiz.entity.DailyProblem;
import com.example.demo.quiz.entity.Problem;
import com.example.demo.quiz.entity.repository.DailyProblemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyProblemCommandService {

  private final DailyProblemRepository dailyProblemRepository;

  /**
   * 주어진 {@link Problem} 문제와 {@link Board} 게시판을 통해 게시판에 문제를 생성합니다.
   *
   * @param problem 등록할 문제
   * @param board 등록할 게시판
   */
  public void createDailyProblem(Problem problem, Board board) {
    DailyProblem dailyProblem =
        DailyProblem.builder().createdAt(LocalDate.now()).problem(problem).board(board).build();

    dailyProblemRepository.save(dailyProblem);
  }
}
