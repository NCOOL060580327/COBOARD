package com.example.demo.board.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardMember;
import com.example.demo.member.entity.Member;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {
  Optional<BoardMember> findByBoardAndMember(Board board, Member member);
}
