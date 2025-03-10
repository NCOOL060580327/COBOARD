package com.example.demo.board.service.query;

import org.springframework.stereotype.Service;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardMember;
import com.example.demo.board.entity.repository.BoardMemberRepository;
import com.example.demo.global.annotation.ReadOnlyTransactional;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.BoardException;
import com.example.demo.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ReadOnlyTransactional
public class BoardMemberQueryService {

  private final BoardMemberRepository boardMemberRepository;

  /**
   * 주어진 게시판과 사용자로 게시판 회원을 조회합니다.
   *
   * @param board 주어진 {@link Board} 게시판
   * @param member 주어진 {@link Member} 사용자
   * @return 조회된 {@link BoardMember} 엔티티
   * @throws BoardException {@code GlobalErrorCode.BOARD_MEMBER_NOT_FOUND} - 해당 게시판과 사용자로 게시판 회원을
   *     조회할 수 없는 경우
   */
  public BoardMember getBoardMemberByBoardAndMember(Board board, Member member) {
    return boardMemberRepository
        .findByBoardAndMember(board, member)
        .orElseThrow(() -> new BoardException(GlobalErrorCode.BOARD_MEMBER_NOT_FOUND));
  }
}
