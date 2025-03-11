package com.example.demo.post.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardMember;
import com.example.demo.board.service.query.BoardMemberQueryService;
import com.example.demo.board.service.query.BoardQueryService;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.PostException;
import com.example.demo.member.entity.Member;
import com.example.demo.post.dto.CreatePostRequestDto;
import com.example.demo.post.dto.GetPostListInBoardResponseDto;
import com.example.demo.post.dto.PostLikeStatusResponseDto;
import com.example.demo.post.entity.Post;
import com.example.demo.post.entity.PostLike;
import com.example.demo.post.service.PostCommandService;
import com.example.demo.post.service.PostLikeCommandService;
import com.example.demo.post.service.PostLikeQueryService;
import com.example.demo.post.service.PostQueryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostFacade {

  private final PostCommandService postCommandService;
  private final PostQueryService postQueryService;
  private final PostLikeCommandService postLikeCommandService;
  private final PostLikeQueryService postLikeQueryService;

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

  /**
   * 주어진 게시판 ID와 회원, 게시글 ID를 통해 게시글 좋아요를 생성합니다.
   *
   * @param boardId 게시글이 있는 게시판 ID
   * @param member 해당 {@link Member} 회원
   * @param postId 좋아요 할 게시글 ID
   * @return 게시글 좋아요 상태 {@link PostLikeStatusResponseDto} (좋아요 상태, 설명)
   */
  @Transactional
  public PostLikeStatusResponseDto createPostLike(Long boardId, Member member, Long postId) {
    Board board = boardQueryService.getBoardById(boardId);

    BoardMember boardMember = boardMemberQueryService.getBoardMemberByBoardAndMember(board, member);

    Post post = postQueryService.getPostByIdWithPessimisticLock(postId);

    if (postLikeQueryService.existsPostLike(post, boardMember)) {
      throw new PostException(GlobalErrorCode.DUPLICATE_LIKE);
    }

    Boolean isLiked = postLikeCommandService.createPostLike(post, boardMember);

    post.increaseLikeCount();

    return new PostLikeStatusResponseDto(isLiked, "좋아요가 추가되었습니다.");
  }

  /**
   * 주어진 게시판 ID와 회원, 게시글 ID를 통해 게시글 좋아요를 삭제합니다.
   *
   * @param boardId 게시글이 있는 게시판 ID
   * @param member 해당 {@link Member} 회원
   * @param postId 좋아요 삭제 할 게시글 ID
   * @return 게시글 좋아요 상태 {@link PostLikeStatusResponseDto} (좋아요 상태, 설명)
   */
  @Transactional
  public PostLikeStatusResponseDto deletePostLike(Long boardId, Member member, Long postId) {
    Board board = boardQueryService.getBoardById(boardId);

    BoardMember boardMember = boardMemberQueryService.getBoardMemberByBoardAndMember(board, member);

    Post post = postQueryService.getPostByIdWithPessimisticLock(postId);

    PostLike postLike = postLikeQueryService.getPostLike(post, boardMember);

    Boolean isLiked = postLikeCommandService.deletePostLike(postLike);

    post.decreaseLikeCount();

    return new PostLikeStatusResponseDto(isLiked, "좋아요가 삭제되었습니다.");
  }
}
