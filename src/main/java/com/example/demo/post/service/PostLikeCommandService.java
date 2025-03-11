package com.example.demo.post.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.board.entity.BoardMember;
import com.example.demo.post.entity.Post;
import com.example.demo.post.entity.PostLike;
import com.example.demo.post.entity.repository.PostLikeJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PostLikeCommandService {

  private final PostLikeJpaRepository postLikeJpaRepository;

  /**
   * 게시글과 게시글 회원을 통해 게시글 좋아요를 생성합니다.
   *
   * @param post 주어진 {@link Post} 게시글
   * @param boardMember 주어진 {@link BoardMember} 게시판 회원
   * @return true 게시판 좋아요 상태 (true = 좋아요 누름)
   */
  public Boolean createPostLike(Post post, BoardMember boardMember) {
    PostLike postLike =
        PostLike.builder()
            .post(post)
            .boardMember(boardMember)
            .createdAt(LocalDateTime.now())
            .build();

    postLikeJpaRepository.save(postLike);

    return true;
  }

  /**
   * 게시글 좋아요를 삭제합니다.
   *
   * @param postLike 주어진 {@link PostLike} 게시글 좋아요
   * @return false 게시글 좋아요 취소 (false = 좋아요 취소/안누름)
   */
  public Boolean deletePostLike(PostLike postLike) {
    postLikeJpaRepository.delete(postLike);
    return false;
  }
}
