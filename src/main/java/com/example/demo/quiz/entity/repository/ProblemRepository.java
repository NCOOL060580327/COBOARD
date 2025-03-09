package com.example.demo.quiz.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.quiz.entity.Problem;

public interface ProblemRepository extends JpaRepository<Problem, Long> {}
