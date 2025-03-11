package com.example.demo.post.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardMember;
import com.example.demo.post.dto.CreatePostRequestDto;
import com.example.demo.post.entity.Post;
import com.example.demo.post.entity.PostCode;
import com.example.demo.post.entity.repository.PostJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PostCommandService {

  private final PostJpaRepository postJpaRepository;

  /**
   * 게시글 생성 요청 데이터를 기반으로 새로운 게시글을 생성합니다.
   *
   * @param requestDto 게시글 생성 요청 데이터 {@link CreatePostRequestDto} (게시글 제목, 코드 언어, 코드 내용)
   * @param boardMember 주어진 {@link BoardMember} 게시판 회원
   */
  public void createPost(CreatePostRequestDto requestDto, BoardMember boardMember, Board board) {
    Post post =
        Post.builder()
            .title(requestDto.title())
            .postCode(createPostCode(requestDto))
            .createdAt(LocalDateTime.now())
            .board(board)
            .boardMember(boardMember)
            .build();

    postJpaRepository.save(post);
  }

  /**
   * 게시글 생성 요청 데이터를 기반으로 게시글의 코드를 생성합니다.
   *
   * @param requestDto 게시글 생성 요청 데이터 {@link CreatePostRequestDto} (게시글 제목, 코드 언어, 코드 내용)
   * @return 생성된 {@link PostCode} 게시글 내 코드
   */
  private PostCode createPostCode(CreatePostRequestDto requestDto) {
    return PostCode.builder().language(requestDto.language()).content(requestDto.content()).build();
  }
}
