package com.example.demo.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.board.dto.BoardCreateRequestDto;
import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.repository.BoardRepository;
import com.example.demo.board.service.BoardCommandService;

@ExtendWith(MockitoExtension.class)
public class BoardCommandServiceTest {

  @Mock private BoardRepository boardRepository;

  @InjectMocks private BoardCommandService boardCommandService;

  private final String testName = BoardTestConst.TEST_NAME.getValue();
  private final String testThumbnailImage = BoardTestConst.TEST_THUMBNAIL.getValue();
  private final String defaultThumbnailImage = BoardTestConst.DEFAULT_THUMBNAIL.getValue();

  @BeforeEach
  void setUp() {}

  @Nested
  @DisplayName("게시판을 생성할 때")
  class createBoard {

    @Test
    @DisplayName("썸네일 이미지가 제공된 경우, 정상적으로 게시판을 생성합니다.")
    void createBoard_WithThumbnail_Success() {
      // given
      BoardCreateRequestDto requestDto =
          new BoardCreateRequestDto(testName, testThumbnailImage, null);

      Board testBoard = Board.builder().name(testName).thumbnailImage(testThumbnailImage).build();

      when(boardRepository.save(any(Board.class))).thenReturn(testBoard);

      // when
      Board board = boardCommandService.createBoard(requestDto);

      // then
      assertNotNull(board, "반환된 게시판이 null이면 안됩니다");
      assertEquals(testName, board.getName(), "게시판 이름이 일치해야 합니다");
      assertEquals(testThumbnailImage, board.getThumbnailImage(), "썸네일 이미지가 일치해야 합니다");
      verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    @DisplayName("썸네일 이미지가 null인 경우, 기본 이미지를 사용해 게시판을 생성합니다.")
    void createBoard_WithoutThumbnail_Success() {
      // given
      BoardCreateRequestDto requestDto = new BoardCreateRequestDto(testName, null, null);

      Board testBoard =
          Board.builder().name(testName).thumbnailImage(defaultThumbnailImage).build();

      when(boardRepository.save(any(Board.class))).thenReturn(testBoard);

      // when
      Board board = boardCommandService.createBoard(requestDto);

      // then
      assertNotNull(board, "반환된 게시판이 null이면 안됩니다");
      assertEquals(testName, board.getName(), "게시판 이름이 일치해야 합니다");
      assertEquals(defaultThumbnailImage, board.getThumbnailImage(), "기본 썸네일 이미지가 설정되어야 합니다");
      verify(boardRepository, times(1)).save(any(Board.class));
    }
  }
}
