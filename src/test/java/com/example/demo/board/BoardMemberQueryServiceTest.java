package com.example.demo.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardMember;
import com.example.demo.board.entity.repository.BoardMemberRepository;
import com.example.demo.board.service.query.BoardMemberQueryService;
import com.example.demo.global.exception.GlobalErrorCode;
import com.example.demo.global.exception.custom.BoardException;
import com.example.demo.member.entity.Member;

@ExtendWith(MockitoExtension.class)
public class BoardMemberQueryServiceTest {

  @Mock private BoardMemberRepository boardMemberRepository;

  @InjectMocks private BoardMemberQueryService boardMemberQueryService;

  @Nested
  @DisplayName("게시판 회원을 조회할 때")
  class getBoardMemberByBoardAndMember {
    @Test
    @DisplayName("게시판 회원을 반환합니다.")
    void getBoardMemberByBoardAndMember_Success() {
      // give
      Board mockBoard = mock(Board.class);
      Member mockMember = mock(Member.class);

      BoardMember testBoardMember =
          BoardMember.builder().board(mockBoard).member(mockMember).isLeader(true).build();

      when(boardMemberRepository.findByBoardAndMember(mockBoard, mockMember))
          .thenReturn(Optional.of(testBoardMember));

      // when
      BoardMember boardMember =
          boardMemberQueryService.getBoardMemberByBoardAndMember(mockBoard, mockMember);

      // then
      assertNotNull(boardMember, "반환된 게시판 회원이 null이면 안됩니다.");
      assertEquals(mockMember, boardMember.getMember(), "회원이 일치해야합니다.");
      assertEquals(mockBoard, boardMember.getBoard(), "게시판이 일치해야합니다.");
      verify(boardMemberRepository, times(1)).findByBoardAndMember(mockBoard, mockMember);
    }

    @Test
    @DisplayName("게시판 회원이 존재하지 않으면 예외를 발생시킵니다.")
    void getBoardMemberByBoardAndMember_Fail_Board_Member_NotFound() {
      // give
      Board mockBoard = mock(Board.class);

      Member mockMember = mock(Member.class);

      when(boardMemberRepository.findByBoardAndMember(mockBoard, mockMember))
          .thenReturn(Optional.empty());

      // when
      BoardException exception =
          assertThrows(
              BoardException.class,
              () -> boardMemberQueryService.getBoardMemberByBoardAndMember(mockBoard, mockMember));

      // then
      assertEquals(
          GlobalErrorCode.BOARD_MEMBER_NOT_FOUND, exception.getErrorCode(), "예외코드가 일치해야합니다.");
      verify(boardMemberRepository, times(1)).findByBoardAndMember(mockBoard, mockMember);
    }
  }
}
