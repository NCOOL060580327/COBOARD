package com.example.demo.board.facade;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.board.dto.request.BoardCreateRequestDto;
import com.example.demo.board.dto.response.GetMyBoardListResponseDto;
import com.example.demo.board.entity.Board;
import com.example.demo.board.service.command.BoardCommandService;
import com.example.demo.board.service.command.BoardMemberCommandService;
import com.example.demo.board.service.query.BoardQueryService;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.query.MemberQueryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BoardFacade {

  private final BoardCommandService boardCommandService;
  private final BoardMemberCommandService boardMemberCommandService;
  private final BoardQueryService boardQueryService;

  private final MemberQueryService memberQueryService;

  private static final int DEFAULT_PAGE_SIZE = 10;

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

  /**
   * 주어진 회원이 참여한 게시판 목록을 조회합니다.
   *
   * @param member 조회할 {@link Member} 회원
   * @param lastBoardId 마지막 조회된 게시판 ID (페이징 용, null 가능)
   * @return 조회한 {@link GetMyBoardListResponseDto} 게시판 정보 목록
   */
  public List<GetMyBoardListResponseDto> getMyBoardList(Member member, Long lastBoardId) {
    return boardQueryService.getMyBoardList(member.getId(), lastBoardId, DEFAULT_PAGE_SIZE);
  }
}
