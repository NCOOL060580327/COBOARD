package com.example.demo.board.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.board.entity.BoardMember;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {}
