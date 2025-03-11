package com.example.demo.post.service;

import org.springframework.stereotype.Service;

import com.example.demo.board.entity.BoardMember;
import com.example.demo.global.annotation.ReadOnlyTransactional;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.PostException;
import com.example.demo.post.entity.Post;
import com.example.demo.post.entity.PostLike;
import com.example.demo.post.entity.repository.PostLikeJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ReadOnlyTransactional
public class PostLikeQueryService {

  private final PostLikeJpaRepository postLikeJpaRepository;

  /**
   * 주어진 게시글, 게시판 회원을 통해 게시글 좋아요를 조회합니다.
   *
   * @param post 주어진 {@link Post} 게시글
   * @param boardMember 주어진 {@link BoardMember} 게시판 회원
   * @return 조회된 {@link PostLike} 게시글 좋아요
   * @throws PostException {@code GlobalErrorCode.POST_LIKE_NOT_FOUND} - 해당 게시글 좋아요가 없는 경우
   */
  public PostLike getPostLike(Post post, BoardMember boardMember) {
    return postLikeJpaRepository
        .findPostLikeByPostAndBoardMember(post, boardMember)
        .orElseThrow(() -> new PostException(GlobalErrorCode.POST_LIKE_NOT_FOUND));
  }

  /**
   * 주어진 게시글, 게시판 회원을 통해 게시글 좋아요가 존재 유무를 반환합니다.
   *
   * @param post 주어진 {@link Post} 게시글
   * @param boardMember 주어진 {@link BoardMember} 게시판 회원
   * @return 게시글 좋아요 존재 유무
   */
  public boolean existsPostLike(Post post, BoardMember boardMember) {
    return postLikeJpaRepository.existsByPostAndBoardMember(post, boardMember);
  }
}
