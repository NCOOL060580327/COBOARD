package com.example.demo.quiz;

import jakarta.persistence.*;

import com.example.demo.board.entity.Board;

import lombok.*;

@Entity
@Table(name = "daily_problem")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class DailyProblem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "daily_problem_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private Problem problem;

  @ManyToOne(fetch = FetchType.LAZY)
  private Board board;
}
