package com.example.demo.post.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.global.annotation.ReadOnlyTransactional;
import com.example.demo.post.dto.GetPostListInBoardResponseDto;
import com.example.demo.post.entity.repository.PostQueryDslRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ReadOnlyTransactional
public class PostQueryService {

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
}
