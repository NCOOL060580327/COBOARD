package com.example.demo.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardMember;
import com.example.demo.board.entity.repository.BoardMemberRepository;
import com.example.demo.board.service.command.BoardMemberCommandService;
import com.example.demo.member.entity.Member;

@ExtendWith(MockitoExtension.class)
public class BoardMemberCommandServiceTest {

  @Mock private BoardMemberRepository boardMemberRepository;

  @InjectMocks private BoardMemberCommandService boardMemberCommandService;

  @Mock private Board testBoard;
  @Mock private Member testMember;

  @BeforeEach
  void setUp() {}

  @Test
  @DisplayName("게시판 회원을 생성합니다.")
  void createBoardMember_Success() {
    // given
    BoardMember boardMember = BoardMember.createLeaderBoardMember(testBoard, testMember);
    when(boardMemberRepository.save(any(BoardMember.class))).thenReturn(boardMember);

    // when
    boardMemberCommandService.createBoardMember(testBoard, testMember);

    // then
    ArgumentCaptor<BoardMember> boardMemberCaptor = ArgumentCaptor.forClass(BoardMember.class);
    verify(boardMemberRepository, times(1)).save(boardMemberCaptor.capture());
    BoardMember savedBoardMember = boardMemberCaptor.getValue();

    assertEquals(testBoard, savedBoardMember.getBoard(), "보드가 일치해야 합니다");
    assertEquals(testMember, savedBoardMember.getMember(), "멤버가 일치해야 합니다");
    assertTrue(savedBoardMember.getIsLeader(), "리더로 설정되어야 합니다");
  }

  @Test
  @DisplayName("게시판에 회원을 초대합니다.")
  void joinBoardMember_Success() {
    // given
    List<Member> memberList = List.of(testMember, mock(Member.class));
    List<BoardMember> boardMemberList =
        memberList.stream()
            .map(member -> BoardMember.createRegularBoardMember(testBoard, member))
            .toList();
    when(boardMemberRepository.saveAll(anyList())).thenReturn(boardMemberList);

    // when
    boardMemberCommandService.joinBoardMember(testBoard, memberList);

    // then
    ArgumentCaptor<List<BoardMember>> boardMemberListCaptor = ArgumentCaptor.forClass(List.class);
    verify(boardMemberRepository, times(1)).saveAll(boardMemberListCaptor.capture());
    List<BoardMember> savedBoardMembers = boardMemberListCaptor.getValue();

    assertEquals(2, savedBoardMembers.size(), "저장된 게시판 회원 수와 입력 리스트 크기가 일치해야 합니다");
    assertEquals(testBoard, savedBoardMembers.getFirst().getBoard(), "게시판이 일치해야 합니다");
    assertEquals(testMember, savedBoardMembers.get(0).getMember(), "첫 번째 멤버가 일치해야 합니다");
    assertFalse(savedBoardMembers.get(0).getIsLeader(), "일반 멤버로 설정되어야 합니다");
    assertEquals(testBoard, savedBoardMembers.get(1).getBoard(), "게시판이 일치해야 합니다");
    assertFalse(savedBoardMembers.get(1).getIsLeader(), "일반 멤버로 설정되어야 합니다");
  }
}
