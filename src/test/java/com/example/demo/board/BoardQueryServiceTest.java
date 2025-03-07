package com.example.demo.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.board.dto.response.GetMyBoardListResponseDto;
import com.example.demo.board.entity.repository.BoardQueryDslRepository;
import com.example.demo.board.service.query.BoardQueryService;

@ExtendWith(MockitoExtension.class)
public class BoardQueryServiceTest {

  @Mock private BoardQueryDslRepository boardQueryDslRepository;

  @InjectMocks private BoardQueryService boardQueryService;

  private final Long testMemberId = Long.parseLong(BoardTestConst.TEST_MEMBER_ID.getValue());
  private final Long testLastBoardId = Long.parseLong(BoardTestConst.TEST_LAST_BOARD_ID.getValue());
  private final int testPageSize = Integer.parseInt(BoardTestConst.TEST_PAGE_SIZE.getValue());

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
}
