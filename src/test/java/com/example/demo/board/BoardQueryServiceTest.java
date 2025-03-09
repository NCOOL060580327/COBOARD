package com.example.demo.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.board.dto.response.GetMyBoardListResponseDto;
import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.repository.BoardQueryDslRepository;
import com.example.demo.board.entity.repository.BoardRepository;
import com.example.demo.board.service.query.BoardQueryService;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.BoardException;

@ExtendWith(MockitoExtension.class)
public class BoardQueryServiceTest {

  @Mock private BoardQueryDslRepository boardQueryDslRepository;

  @Mock private BoardRepository boardRepository;

  @InjectMocks private BoardQueryService boardQueryService;

  private final Long testBoardId = Long.parseLong(BoardTestConst.TEST_ID.getValue());
  private final Long testMemberId = Long.parseLong(BoardTestConst.TEST_MEMBER_ID.getValue());
  private final Long testLastBoardId = Long.parseLong(BoardTestConst.TEST_LAST_BOARD_ID.getValue());
  private final int testPageSize = Integer.parseInt(BoardTestConst.TEST_PAGE_SIZE.getValue());
  private final String testName = BoardTestConst.TEST_NAME.getValue();
  private final String testThumbnail = BoardTestConst.TEST_THUMBNAIL.getValue();

  @BeforeEach
  void setUp() {}

  @Nested
  @DisplayName("내가 속한 게시판을 조회 할 때")
  class GetMyBoardList {
    @Test
    @DisplayName("정상적으로 게시판 목록을 조회합니다.")
    void getMyBoardList_Success() {
      // give
      List<GetMyBoardListResponseDto> getMyBoardListResponseDtoList =
          List.of(
              new GetMyBoardListResponseDto(1L, "Board1", "thumbnail1.png"),
              new GetMyBoardListResponseDto(2L, "Board2", "thumbnail2.png"));
      when(boardQueryDslRepository.findBoardListByMemberId(
              testMemberId, testLastBoardId, testPageSize))
          .thenReturn(getMyBoardListResponseDtoList);

      // when
      List<GetMyBoardListResponseDto> testList =
          boardQueryService.getMyBoardList(testMemberId, testLastBoardId, testPageSize);

      // then
      assertNotNull(testList, "반환된 리스트는 null이면 안됩니다");
      assertEquals(2, testList.size(), "리스트 크기가 일치해야 합니다");
      assertEquals(getMyBoardListResponseDtoList, testList, "반환된 리스트가 예상과 일치해야 합니다");
      verify(boardQueryDslRepository, times(1))
          .findBoardListByMemberId(testMemberId, testLastBoardId, testPageSize);
    }

    @Test
    @DisplayName("게시판이 없으면 빈 리스트를 반환합니다")
    void getMyBoardList_Success_EmptyList() {
      // give
      when(boardQueryDslRepository.findBoardListByMemberId(
              testMemberId, testLastBoardId, testPageSize))
          .thenReturn(Collections.emptyList());

      // when
      List<GetMyBoardListResponseDto> result =
          boardQueryService.getMyBoardList(testMemberId, testLastBoardId, testPageSize);

      // then
      assertNotNull(result, "반환된 리스트는 null이면 안됩니다");
      assertTrue(result.isEmpty(), "빈 리스트가 반환되어야 합니다");
      verify(boardQueryDslRepository, times(1))
          .findBoardListByMemberId(testMemberId, testLastBoardId, testPageSize);
    }
  }

  @Nested
  @DisplayName("게시판 ID를 통해 게시판을 조회할 떄")
  class getBoardById {
    @Test
    @DisplayName("게시판을 반환합니다.")
    void getBoardById_Success() {
      // give
      Board testBoard = Board.builder().name(testName).thumbnailImage(testThumbnail).build();

      when(boardRepository.findById(testBoardId)).thenReturn(Optional.of(testBoard));

      // when
      Board board = boardQueryService.getBoardById(testBoardId);

      // then
      assertNotNull(board, "반환된 게시판이 null이면 안됩니다.");
      assertEquals(testName, board.getName(), "게시판 제목이 일치해야힙니다.");
      assertEquals(testThumbnail, board.getThumbnailImage(), "게시판 썸네일이 일치해야합니다.");
      verify(boardRepository, times(1)).findById(testBoardId);
    }

    @Test
    @DisplayName("게시판이 존재하지 않으면 예외를 발생시킵니다.")
    void getBoardById_Fail_Board_Not_Found() {
      // give
      when(boardRepository.findById(testBoardId)).thenReturn(Optional.empty());

      // when
      BoardException exception =
          assertThrows(BoardException.class, () -> boardQueryService.getBoardById(testBoardId));

      // when
      assertEquals(GlobalErrorCode.BOARD_NOT_FOUND, exception.getErrorCode(), "예외코드가 일치해야합니다.");
      verify(boardRepository, times(1)).findById(testBoardId);
    }
  }
}
