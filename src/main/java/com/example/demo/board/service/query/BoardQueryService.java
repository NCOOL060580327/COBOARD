package com.example.demo.board.service.query;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.board.dto.response.GetMyBoardListResponseDto;
import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.repository.BoardQueryDslRepository;
import com.example.demo.board.entity.repository.BoardRepository;
import com.example.demo.global.annotation.ReadOnlyTransactional;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.BoardException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ReadOnlyTransactional
public class BoardQueryService {

  private final BoardRepository boardRepository;
  private final BoardQueryDslRepository boardQueryDslRepository;

  /**
   * 회원이 참여한 게시판 목록을 조회합니다.
   *
   * @param memberId 조회할 회원 ID
   * @param lastBoardId 마지막 조회된 게시판 ID (페이징 용)
   * @param pageSize 페이지 크기
   * @return 조회한 {@link GetMyBoardListResponseDto} 게시판 정보 목록
   */
  public List<GetMyBoardListResponseDto> getMyBoardList(
      Long memberId, Long lastBoardId, int pageSize) {
    return boardQueryDslRepository.findBoardListByMemberId(memberId, lastBoardId, pageSize);
  }

  /**
   * 주어진 게시판 ID를 통해 게시판을 조회합니다.
   *
   * @param boardId 조회할 게시판 ID
   * @return 조회한 {@link Board} 엔티티
   * @throws BoardException {@code GlobalErrorCode.Board_NOT_FOUND} - 해당 게시판을 찾을 수 없을 경우
   */
  public Board getBoardById(Long boardId) {
    return boardRepository
        .findById(boardId)
        .orElseThrow(() -> new BoardException(GlobalErrorCode.BOARD_NOT_FOUND));
  }
}
