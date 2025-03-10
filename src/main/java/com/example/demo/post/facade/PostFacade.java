package com.example.demo.post.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardMember;
import com.example.demo.board.service.query.BoardMemberQueryService;
import com.example.demo.board.service.query.BoardQueryService;
import com.example.demo.member.entity.Member;
import com.example.demo.post.dto.CreatePostRequestDto;
import com.example.demo.post.dto.GetPostListInBoardResponseDto;
import com.example.demo.post.service.PostCommandService;
import com.example.demo.post.service.PostQueryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostFacade {

  private final PostCommandService postCommandService;
  private final PostQueryService postQueryService;

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

    postCommandService.createPost(requestDto, boardMember, board);
  }

  /**
   * 주어진 게시판 ID와 페이지 요청 정보를 기반으로 해당 게시판 내의 게시글 목록을 조회합니다.
   *
   * @param boardId 게시글을 찾을 게시판 ID
   * @param pageable 페이지네이션 및 정렬 정보를 포함하는 객체
   * @return 해당 게시판의 게시글 목록을 포함하는 {@link Page} 객체
   */
  public Page<GetPostListInBoardResponseDto> getPostListInBoard(Long boardId, Pageable pageable) {
    return postQueryService.getPostListInBoard(boardId, pageable);
  }
}
