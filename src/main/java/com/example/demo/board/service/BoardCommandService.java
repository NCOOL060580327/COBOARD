package com.example.demo.board.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.board.dto.BoardCreateRequestDto;
import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardCommandService {

  private final BoardRepository boardRepository;

  /**
   * 게시판 생성 요청 데이터를 기반으로 새로운 게시판을 생성합니다.
   *
   * @param requestDto 게시판 생성 요청 데이터 {@link BoardCreateRequestDto} (게시판 이름, 썸네일, 초대 회원)
   * @return 저장된 {@link Board} 엔티티
   */
  public Board createBoard(BoardCreateRequestDto requestDto) {

    Board board =
        Board.builder()
            .name(requestDto.name())
            .thumbnailImage(
                (requestDto.thumbnailImage() != null)
                    ? requestDto.thumbnailImage()
                    : "default-thumbnail.png")
            .build();

    return boardRepository.save(board);
  }
}
