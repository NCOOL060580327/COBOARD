package com.example.demo.quiz.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.quiz.entity.DailyProblem;

public interface DailyProblemRepository extends JpaRepository<DailyProblem, Long> {}
