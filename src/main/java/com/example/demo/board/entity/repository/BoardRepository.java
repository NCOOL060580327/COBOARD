package com.example.demo.board.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {}
