package com.example.demo.board.service.command;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardMember;
import com.example.demo.board.entity.repository.BoardMemberRepository;
import com.example.demo.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardMemberCommandService {

  private final BoardMemberRepository boardMemberRepository;

  /**
   * 주어진 게시판과 회원 정보를 기반으로 새로운 방장 게시판 회원을 생성합니다.
   *
   * @param board 리더로 등록할 {@link Board} 엔티티
   * @param member 리더로 등록될 {@link Member} 엔티티
   */
  public void createBoardMember(Board board, Member member) {

    BoardMember boardMember = BoardMember.createLeaderBoardMember(board, member);

    boardMemberRepository.save(boardMember);
  }

  /**
   * 주어진 게시판과 회원 목록을 기반으로 일반 게시판 회원을 추가합니다.
   *
   * @param board 회원을 추가할 {@link Board} 엔티티
   * @param memberList 추가할 {@link Member} 엔티티 리스트
   */
  public void joinBoardMember(Board board, List<Member> memberList) {
    List<BoardMember> boardMemberList =
        memberList.stream()
            .map(member -> BoardMember.createRegularBoardMember(board, member))
            .toList();
    boardMemberRepository.saveAll(boardMemberList);
  }
}
