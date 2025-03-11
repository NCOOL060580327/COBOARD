package com.example.demo.post.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.global.annotation.ReadOnlyTransactional;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.PostException;
import com.example.demo.post.dto.GetPostListInBoardResponseDto;
import com.example.demo.post.entity.Post;
import com.example.demo.post.entity.repository.PostJpaRepository;
import com.example.demo.post.entity.repository.PostQueryDslRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ReadOnlyTransactional
public class PostQueryService {

  private final PostJpaRepository postJpaRepository;
  private final PostQueryDslRepository postQueryDslRepository;

  /**
   * 주어진 게시판 ID와 페이지 요청 정보를 기반으로 해당 게시판 내의 게시글 목록을 조회합니다.
   *
   * @param boardId 게시글을 찾을 게시판 ID
   * @param pageable 페이지네이션 및 정렬 정보를 포함하는 객체
   * @return 해당 게시판의 게시글 목록을 포함하는 {@link Page} 객체
   */
  public Page<GetPostListInBoardResponseDto> getPostListInBoard(Long boardId, Pageable pageable) {
    return postQueryDslRepository.findPostListByBoard(boardId, pageable);
  }

  /**
   * 주어진 게시글 ID를 통해 해당 게시글을 반환합니다.
   *
   * @param postId 조회할 게시글 ID
   * @return 조회된 {@link Post} 게시글
   * @throws PostException {@code GlobalErrorCode.POST_NOT_FOUND} - 헤당 게시글이 없는 경우
   */
  public Post getPostById(Long postId) {
    return postJpaRepository
        .findById(postId)
        .orElseThrow(() -> new PostException(GlobalErrorCode.POST_NOT_FOUND));
  }

  /**
   * 주어진 게시글 ID를 통해 해당 게시글을 반환합니다. (비관적 락 적용)
   *
   * @param postId 조회할 게시글 ID
   * @return 조회된 {@link Post} 게시글
   * @throws PostException {@code GlobalErrorCode.POST_NOT_FOUND} - 헤당 게시글이 없는 경우
   */
  @Transactional
  public Post getPostByIdWithPessimisticLock(Long postId) {
    return postJpaRepository
        .findByIdWithPessimisticLock(postId)
        .orElseThrow(() -> new PostException(GlobalErrorCode.POST_NOT_FOUND));
  }
}
