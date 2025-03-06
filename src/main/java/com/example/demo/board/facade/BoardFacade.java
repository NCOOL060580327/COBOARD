package com.example.demo.board.facade;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.board.dto.BoardCreateRequestDto;
import com.example.demo.board.entity.Board;
import com.example.demo.board.service.BoardCommandService;
import com.example.demo.board.service.BoardMemberCommandService;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.query.MemberQueryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BoardFacade {

  private final BoardCommandService boardCommandService;
  private final BoardMemberCommandService boardMemberCommandService;
  private final MemberQueryService memberQueryService;

  /**
   * 주어진 요청 데이터를 기반으로 새로운 게시판을 생성하고, 게시판 방장 및 회원을 등록합니다.
   *
   * @param requestDto 게시판 생성 요청 데이터 {@link BoardCreateRequestDto} (게시판 이름, 썸네일, 초대 회원 목록)
   * @param member 게시판을 생성하는 {@link Member} (리더로 등록될 회원)
   * @return 생성 및 저장된 {@link Board} 엔티티
   */
  @Transactional
  public Board createBoard(BoardCreateRequestDto requestDto, Member member) {
    Board board = boardCommandService.createBoard(requestDto);

    boardMemberCommandService.createBoardMember(board, member);

    List<Member> memberList =
        memberQueryService.getMemberListByIdentifiedCodeResponseDtoList(
            requestDto.responseDtoList());

    boardMemberCommandService.joinBoardMember(board, memberList);

    return board;
  }
}
