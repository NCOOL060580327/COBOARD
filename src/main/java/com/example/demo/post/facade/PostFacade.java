package com.example.demo.post.facade;

import org.springframework.stereotype.Component;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardMember;
import com.example.demo.board.service.query.BoardMemberQueryService;
import com.example.demo.board.service.query.BoardQueryService;
import com.example.demo.member.entity.Member;
import com.example.demo.post.dto.CreatePostRequestDto;
import com.example.demo.post.service.PostCommandService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostFacade {

  private final PostCommandService postCommandService;

  private final BoardQueryService boardQueryService;
  private final BoardMemberQueryService boardMemberQueryService;

  /**
   * 주어진 요청 데이터를 기반으로 새로운 게시글을 생성합니다.
   *
   * @param requestDto 게시글 생성 요청 데이터 {@link CreatePostRequestDto} (게시글 제목, 코드 언어, 코드 내용)
   * @param boardId 작성할 게시판 ID
   * @param member 작성하는 사용자
   */
  public void createPost(CreatePostRequestDto requestDto, Long boardId, Member member) {

    Board board = boardQueryService.getBoardById(boardId);

    BoardMember boardMember = boardMemberQueryService.getBoardMemberByBoardAndMember(board, member);

    postCommandService.createPost(requestDto, boardMember);
  }
}
